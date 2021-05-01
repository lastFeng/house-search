package com.example.housesearch.service;

import com.example.housesearch.domain.House;
import com.example.housesearch.domain.HouseDetail;
import com.example.housesearch.domain.HouseTag;
import com.example.housesearch.domain.SupportAddress;
import com.example.housesearch.domain.base.ServiceResult;
import com.example.housesearch.domain.search.*;
import com.example.housesearch.utils.Constant;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.rest.RestStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
}
