package com.example.housesearch.service;

import com.example.housesearch.domain.House;
import com.example.housesearch.domain.HouseDetail;
import com.example.housesearch.domain.HouseTag;
import com.example.housesearch.domain.SupportAddress;
import com.example.housesearch.domain.base.ServiceMultiResult;
import com.example.housesearch.domain.base.ServiceResult;
import com.example.housesearch.domain.search.*;
import com.example.housesearch.utils.Constant;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeAction;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;

import static com.example.housesearch.utils.Constant.*;

/**
 * @author : guoweifeng
 * @date : 2021/5/1
 */
@Service
@Slf4j
public class SearchService {
    @Autowired
    private HouseService houseService;

    @Autowired
    private HouseDetailService houseDetailService;

    @Autowired
    private HouseTagService houseTagService;

    @Autowired
    private SupportAddressService supportAddressService;

    @Autowired
    private BaiduAddressService baiduAddressService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TransportClient esClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = HOUSE_TOPIC)
    private void handleMessage(String content) {
        try {
            HouseIndexMessage message = objectMapper.readValue(content, HouseIndexMessage.class);
            switch (message.getOperation()) {
                case HouseIndexMessage.INDEX:
                    createOrUpdate(message);
                    break;
                case HouseIndexMessage.REMOVE:
                    removeIndex(message);
                    break;
                default:
                    log.warn("not support message content");
                    break;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void createOrUpdate(HouseIndexMessage message) {
        Integer houseId = message.getHouseId();
        House house = houseService.findById(houseId);
        if (house == null) {
            log.info("Index House {} does not exists, Create Index", houseId);
            index(houseId, message.getRetry() + 1);
            return;
        }
        HouseIndex houseIndex = modelMapper.map(house, HouseIndex.class);
        HouseDetail detail = houseDetailService.findByHouseId(houseId).get();
        modelMapper.map(detail, houseIndex);

        SupportAddress city = supportAddressService.findByEnNameAndLevel(house.getCityEnName(), Constant.CityLevel.CITY.getValue());
        SupportAddress region = supportAddressService.findByEnNameAndLevel(house.getRegionEnName(), Constant.CityLevel.REGION.getValue());
        String address = city.getCnName() + region.getCnName() + house.getStreet() + house.getDirection() + detail.getDetailAddress();

        ServiceResult<BaiduMapLocation> location = baiduAddressService.getBaiduMapLocation(city.getCnName(), address);
        if (!location.isSuccess()) {
            index(message.getHouseId(), message.getRetry() + 1);
            return;
        }
        houseIndex.setLocation(location.getResult());

        List<HouseTag> tags = houseTagService.findAllByHouseId(houseId);
        if (!CollectionUtils.isEmpty(tags)) {
            List<String> strings = new ArrayList<>();
            tags.forEach(item -> strings.add(item.getName()));
            houseIndex.setTags(strings);
        }

        SearchRequestBuilder requestBuilder = esClient.prepareSearch(HOUSE_INDEX_NAME).setTypes(HOUSE_INDEX_TYPE)
                .setQuery(QueryBuilders.termQuery(HouseIndexKey.HOUSE_ID, houseId));
        SearchResponse response = requestBuilder.get();
        boolean success;
        long totalHits = response.getHits().getTotalHits();
        if (totalHits != 0) {
            success = create(houseIndex);
        } else if (totalHits == 1) {
            String esId = response.getHits().getAt(0).getId();
            success = update(esId, houseIndex);
        } else {
            success = deleteAndCreate(totalHits, houseIndex);
        }

        ServiceResult serviceResult = baiduAddressService.lbsUpload(location.getResult(), house.getStreet() + house.getDistrict(),
                city.getCnName() + region.getCnName() + house.getStreet() + house.getDistrict(),
                message.getHouseId(), house.getPrice(), house.getArea());

        if (!serviceResult.isSuccess()) {
            index(message.getHouseId(), message.getRetry() + 1);
        }
    }

    private boolean deleteAndCreate(long totalHits, HouseIndex houseIndex) {
        DeleteByQueryRequestBuilder builder = DeleteByQueryAction.INSTANCE
                .newRequestBuilder(esClient)
                .filter(QueryBuilders.termQuery(HouseIndexKey.HOUSE_ID, houseIndex.getHouseId()))
                .source(HOUSE_INDEX_NAME);
        BulkByScrollResponse response = builder.get();
        long deleted = response.getDeleted();
        if (deleted != totalHits) {
            return false;
        } else {
            return create(houseIndex);
        }
    }

    private boolean update(String esId, HouseIndex houseIndex) {
        if (updateSuggest(houseIndex)) {
            return false;
        }

        try {
            UpdateResponse response = esClient.prepareUpdate(HOUSE_INDEX_NAME, HOUSE_INDEX_TYPE, esId)
                    .setDoc(objectMapper.writeValueAsBytes(houseIndex), XContentType.JSON).get();
            return response.status() == RestStatus.OK;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean create(HouseIndex houseIndex) {
        if (updateSuggest(houseIndex)) {
            return false;
        }

        try {
            IndexResponse response = esClient.prepareIndex(HOUSE_INDEX_NAME, HOUSE_INDEX_TYPE)
                    .setSource(objectMapper.writeValueAsBytes(houseIndex), XContentType.JSON).get();
            return response.status() == RestStatus.CREATED;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean updateSuggest(HouseIndex houseIndex) {
        ElasticsearchClient client;
        AnalyzeAction analyzeAction;
        AnalyzeRequestBuilder requestBuilder = new AnalyzeRequestBuilder(esClient,
                AnalyzeAction.INSTANCE, HOUSE_INDEX_NAME, houseIndex.getTitle(),
                houseIndex.getLayoutDesc(), houseIndex.getRoundService(),
                houseIndex.getDescription(), houseIndex.getSubwayLineName(),
                houseIndex.getSubwayStationName());

        requestBuilder.setAnalyzer("ik_smart");
        AnalyzeResponse response = requestBuilder.get();
        List<AnalyzeResponse.AnalyzeToken> tokens = response.getTokens();
        if (tokens == null) {
            log.warn("Can not analyze token for house:" + houseIndex.getHouseId());
            return true;
        }

        List<HouseSuggest> suggests = new ArrayList<>();
        tokens.forEach(token -> {
            if ("<NUM>".equals(token.getType()) || token.getTerm().length() < 2) {
                return;
            }

            HouseSuggest suggest = new HouseSuggest();
            suggest.setInput(token.getTerm());
            suggests.add(suggest);
        });

        // 定制化小区不全
        HouseSuggest suggest = new HouseSuggest();
        suggest.setInput(houseIndex.getDistrict());
        suggests.add(suggest);

        houseIndex.setSuggest(suggests);
        return false;
    }

    private void removeIndex(HouseIndexMessage message) {
        Integer houseId = message.getHouseId();
        DeleteByQueryRequestBuilder builder = DeleteByQueryAction.INSTANCE
                .newRequestBuilder(esClient)
                .filter(QueryBuilders.termQuery(HouseIndexKey.HOUSE_ID, houseId))
                .source(HOUSE_INDEX_NAME);
        BulkByScrollResponse response = builder.get();
        long deleted = response.getDeleted();
        if (log.isDebugEnabled()) {
            log.debug("delete total={} by houseId={}", deleted, houseId);
        }

        ServiceResult serviceResult = baiduAddressService.removeLbs(houseId);

        if (!serviceResult.isSuccess() || deleted <= 0) {
            this.removeIndex(houseId, message.getRetry() + 1);
        }
    }

    private void index(Integer id, int retry) {
        if (retry > HouseIndexMessage.MAX_RETRY) {
            log.error("Retry index times over {} for house {}, Please check it", HouseIndexMessage.MAX_RETRY, id);
            return;
        }
        HouseIndexMessage message = new HouseIndexMessage(id, HouseIndexMessage.INDEX, retry);
        try {
            this.kafkaTemplate.send(HOUSE_TOPIC, objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            log.error("send to kafka message={} error", message);
        }
    }

    private void removeIndex(Integer id, int retry) {
        if (retry > HouseIndexMessage.MAX_RETRY) {
            log.error("Retry remove times over {} for house {}, Please check it", HouseIndexMessage.MAX_RETRY, id);
            return;
        }

        HouseIndexMessage message = new HouseIndexMessage(id, HouseIndexMessage.REMOVE, retry);
        try {
            this.kafkaTemplate.send(HOUSE_TOPIC, objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            log.error("send to kafka message={} error", message);
        }
    }

    /**
     * 通过某些搜索，搜索出房源id
     * @param rentSearch
     * @return
     */
    public ServiceMultiResult<Integer> query(RentSearch rentSearch) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // 设置城市
        boolQuery.filter(QueryBuilders.termQuery(HouseIndexKey.CITY_EN_NAME, rentSearch.getCityEnName()));
        // 设置区域
        if (rentSearch.getRegionEnName() != null && !"*".equals(rentSearch.getRegionEnName())) {
            boolQuery.filter(QueryBuilders.termQuery(HouseIndexKey.REGION_EN_NAME, rentSearch.getRegionEnName()));
        }

        // 设置区域范围
        RentValueBlock area = RentValueBlock.matchArea(rentSearch.getAreaBlock());
        if (!Objects.equals(RentValueBlock.ALL, area)) {
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(HouseIndexKey.AREA);
            if (area.getMax() > 0) {
                rangeQueryBuilder.lte(area.getMax());
            }
            if (area.getMin() > 0) {
                rangeQueryBuilder.gte(area.getMin());
            }
            boolQuery.filter(rangeQueryBuilder);
        }
        // 设置房价范围
        RentValueBlock price = RentValueBlock.matchPrice(rentSearch.getPriceBlock());
        if (!Objects.equals(RentValueBlock.ALL, price)) {
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(HouseIndexKey.PRICE);
            if (price.getMax() > 0) {
                rangeQueryBuilder.lte(price.getMax());
            }
            if (price.getMin() > 0) {
                rangeQueryBuilder.gte(price.getMin());
            }
            boolQuery.filter(rangeQueryBuilder);
        }
        // 设置租房方式
        if (rentSearch.getRentWay() > -1) {
            boolQuery.filter(QueryBuilders.termQuery(HouseIndexKey.RENT_WAY, rentSearch.getRentWay()));
        }
        // 设置房屋朝向
        if (rentSearch.getDirection() > 0) {
            boolQuery.filter(QueryBuilders.termQuery(HouseIndexKey.DIRECTION, rentSearch.getDirection()));
        }
        // 关键词查找
        boolQuery.must(QueryBuilders.multiMatchQuery(rentSearch.getKeywords(), HouseIndexKey.TITLE, HouseIndexKey.TRAFFIC,
                HouseIndexKey.DISTRICT, HouseIndexKey.ROUND_SERVICE, HouseIndexKey.SUBWAY_LINE_NAME, HouseIndexKey.SUBWAY_STATION_NAME));

        // 构件
        SearchRequestBuilder request = esClient.prepareSearch(HOUSE_INDEX_NAME).setTypes(HOUSE_INDEX_TYPE)
                .setQuery(boolQuery)
                .addSort(HouseSort.getSortKey(rentSearch.getOrderBy()), SortOrder.fromString(rentSearch.getOrderDirection()))
                .setFrom(rentSearch.getStart())
                .setSize(rentSearch.getSize())
                .setFetchSource(HouseIndexKey.HOUSE_ID, null);

        // 获取返回值
        SearchResponse response = request.get();
        List<Integer> houseIds = new ArrayList<>();

        if (response.status() != RestStatus.OK) {
            return ServiceMultiResult.<Integer>builder().total(0L).result(houseIds).build();
        }

        for (SearchHit hit : response.getHits()) {
            houseIds.add(Integer.parseInt(String.valueOf(hit.getSourceAsMap().get(HouseIndexKey.HOUSE_ID))));
        }
        return ServiceMultiResult.<Integer>builder().total(response.getHits().totalHits).result(houseIds).build();
    }

    /***
     * 获取提示内容
     * @param prefix
     * @return
     */
    public ServiceResult<List<String>> suggest(String prefix) {
        String suggestionName = "autocomplete";
        int maxSize = 5;
        // 设置完全补全的配置
        CompletionSuggestionBuilder suggestionBuilder = SuggestBuilders.completionSuggestion("suggest")
                .prefix(prefix).size(maxSize);
        // 设置builder
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion(suggestionName, suggestionBuilder);
        // 配置请求
        SearchRequestBuilder request = esClient.prepareSearch(HOUSE_INDEX_NAME).setTypes(HOUSE_INDEX_TYPE).suggest(suggestBuilder);

        // 获得返回结果
        Suggest suggest = request.get().getSuggest();
        if (suggest == null) {
            return ServiceResult.<List<String>>builder().success(true).result(new ArrayList<>()).build();
        }

        Suggest.Suggestion result = suggest.getSuggestion(suggestionName);
        int maxSuggest = 0;
        Set<String> suggestSet = new HashSet<>();
        for (Object entry : result.getEntries()) {
            if (entry instanceof CompletionSuggestion.Entry) {
                CompletionSuggestion.Entry item = (CompletionSuggestion.Entry)entry;
                if (item.getOptions().isEmpty()) {
                    continue;
                }
                for (CompletionSuggestion.Entry.Option option : item.getOptions()) {
                    String tip = option.getText().string();
                    if (suggestSet.contains(tip)) {
                        continue;
                    }
                    suggestSet.add(tip);
                    maxSuggest++;
                }
            }
            if (maxSuggest > maxSize) {
                break;
            }
        }
        List<String> list = Lists.newArrayList(suggestSet);
        return ServiceResult.<List<String>>builder().success(true).result(list).build();
    }

    /***
     * 聚合查询街区房源
     */
    public ServiceResult<Integer> aggregationDistrictHouse(String cityEnName, String regionEnName, String district) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery(HouseIndexKey.CITY_EN_NAME, cityEnName))
                .filter(QueryBuilders.termQuery(HouseIndexKey.REGION_EN_NAME, regionEnName))
                .filter(QueryBuilders.termQuery(HouseIndexKey.DISTRICT, district));

        SearchRequestBuilder request = esClient.prepareSearch(HOUSE_INDEX_NAME).setTypes(HOUSE_INDEX_TYPE)
                .setQuery(boolQuery)
                .addAggregation(AggregationBuilders.terms(HouseIndexKey.AGG_DISTRICT).field(HouseIndexKey.DISTRICT))
                .setSize(0);
        SearchResponse response = request.get();

        if (response.status() == RestStatus.OK) {
            Terms aggregation = response.getAggregations().get(HouseIndexKey.AGG_DISTRICT);
            if (!CollectionUtils.isEmpty(aggregation.getBuckets())) {
                return ServiceResult.<Integer>builder().success(true).result((int) aggregation.getBucketByKey(district).getDocCount()).build();
            }
        }
        return ServiceResult.<Integer>builder().success(true).result(0).build();
    }

    /**
     * 地图的聚合查询,通过给定的城市名与区域
     */
    public ServiceMultiResult<HouseBucketDTO> mapAggregation(String cityEnName) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.termQuery(HouseIndexKey.CITY_EN_NAME, cityEnName));

        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms(HouseIndexKey.AGG_REGION).field(HouseIndexKey.REGION_EN_NAME);
        SearchRequestBuilder request = esClient.prepareSearch(HOUSE_INDEX_NAME).setTypes(HOUSE_INDEX_TYPE).setQuery(boolQueryBuilder)
                .addAggregation(aggregationBuilder);
        SearchResponse response = request.get();
        List<HouseBucketDTO> result = new ArrayList<>();
        if (response.status() == RestStatus.OK) {
            Terms aggregation = response.getAggregations().get(HouseIndexKey.AGG_REGION);
            for (Terms.Bucket bucket : aggregation.getBuckets()) {
                result.add(new HouseBucketDTO(bucket.getKeyAsString(), bucket.getDocCount()));
            }
        }
        return ServiceMultiResult.<HouseBucketDTO>builder().total(CollectionUtils.isEmpty(result) ? 0L : response.getHits().getTotalHits())
                .result(result).build();
    }

    /**
     * 通过给定字段查询
     * @param cityEnName
     * @param orderBy
     * @param orderDirection
     * @param start
     * @param size
     * @return
     */
    public ServiceMultiResult<Integer> mapQuery(String cityEnName, String orderBy, String orderDirection, int start, int size) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.termQuery(HouseIndexKey.CITY_EN_NAME, cityEnName));

        SearchRequestBuilder request = esClient.prepareSearch(HOUSE_INDEX_NAME).setTypes(HOUSE_INDEX_TYPE)
                .setQuery(boolQueryBuilder)
                .addSort(HouseSort.getSortKey(orderBy), SortOrder.fromString(orderDirection))
                .setFrom(start)
                .setSize(size);

        SearchResponse response = request.get();
        List<Integer> list = new ArrayList<>();

        if (response.status() == RestStatus.OK) {
            for (SearchHit hit : response.getHits()) {
                list.add(Integer.parseInt(String.valueOf(hit.getSourceAsMap().get(HouseIndexKey.HOUSE_ID))));
            }
        }
        return ServiceMultiResult.<Integer>builder().total(CollectionUtils.isEmpty(list) ? 0L : response.getHits().getTotalHits())
                .result(list).build();
    }

    /***
     * 通过Map搜索对象来查询
     * @param mapSearch
     * @return
     */
    public ServiceMultiResult<Integer> mapQuery(MapSearch mapSearch) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder.filter(QueryBuilders.termQuery(HouseIndexKey.CITY_EN_NAME, mapSearch.getCityEnName()));
        boolQueryBuilder.filter(
                QueryBuilders.geoBoundingBoxQuery("location")
                .setCorners(
                        new GeoPoint(mapSearch.getLeftLatitude(), mapSearch.getLeftLongitude()),
                        new GeoPoint(mapSearch.getRightLatitde(), mapSearch.getRightLongitude())));
        SearchResponse response = esClient.prepareSearch(HOUSE_INDEX_NAME).setTypes(HOUSE_INDEX_TYPE)
                .setQuery(boolQueryBuilder)
                .addSort(HouseSort.getSortKey(mapSearch.getOrderBy()), SortOrder.fromString(mapSearch.getOrderDirection()))
                .setFrom(mapSearch.getSize())
                .setSize(mapSearch.getSize()).get();

        List<Integer> list = new ArrayList<>();
        if (response.status() == RestStatus.OK) {
            for (SearchHit hit : response.getHits()) {
                list.add(Integer.parseInt(String.valueOf(hit.getSourceAsMap().get(HouseIndexKey.HOUSE_ID))));
            }
        }
        return ServiceMultiResult.<Integer>builder().total(CollectionUtils.isEmpty(list) ? 0L : response.getHits().getTotalHits())
                .result(list).build();
    }
}
