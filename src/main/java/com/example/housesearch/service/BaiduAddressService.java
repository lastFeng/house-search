package com.example.housesearch.service;

import com.example.housesearch.domain.SupportAddress;
import com.example.housesearch.domain.base.ServiceMultiResult;
import com.example.housesearch.domain.base.ServiceResult;
import com.example.housesearch.domain.dto.SupportAddressDTO;
import com.example.housesearch.domain.search.BaiduMapLocation;
import com.example.housesearch.utils.Constant;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : guoweifeng
 * @date : 2021/5/1
 */
@Service
@Slf4j
public class BaiduAddressService {

    @Autowired
    private SupportAddressService supportAddressService;

    @Autowired
    private SubwayService subwayService;

    @Autowired
    private SubwayStationService subwayStationService;

    @Autowired
    private ModelMapper modelMapper;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String BAIDU_MAP_KEY = "baidu_map_key";
    private static final String BAIDU_MAP_GEOONV_API = "http://api.map.baidu.com/geocoder/v2/?";

    /***
     * POST数据管理接口
     */
    private static final String LBS_CREATE_API = "http://api.map.baidu.com/geodat/v3/poi/create";
    private static final String LBS_QUERY_API = "http://api.map.baidu.com/geodat/v3/poi/list?";
    private static final String LBS_UPDATE_API = "http://api.map.baidu.com/geodat/v3/poi/update";
    private static final String LBS_DELETE_API = "http://api.map.baidu.com/geodat/v3/poi/delete";

    public ServiceResult removeLbs(Integer houseId) {
        HttpClient httpClient = HttpClients.createDefault();
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("geotable_id", "xxxx"));
        nameValuePairs.add(new BasicNameValuePair("ak", BAIDU_MAP_KEY));
        nameValuePairs.add(new BasicNameValuePair("houseId", String.valueOf(houseId)));

        HttpPost post = new HttpPost(LBS_DELETE_API);
        try {
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = httpClient.execute(post);
            String resultResponse = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                JsonNode jsonNode = objectMapper.readTree(resultResponse);
                int status = jsonNode.get("status").asInt();
                if (status == 0) {
                    return ServiceResult.builder().success(true).message("sucess").build();
                }
            }
        } catch (Exception e) {
            log.error("Error");
            return ServiceResult.builder().success(false).message("Error").build();
        }

        return ServiceResult.builder().success(false).message("Error").build();
    }

    /***
     * 获取百度地图位置
     * @param cnName
     * @param address
     * @return
     */
    public ServiceResult<BaiduMapLocation> getBaiduMapLocation(String cnName, String address) {
        String encodeAddress;
        String encodeCity;

        try {
            encodeAddress = URLEncoder.encode(address, "UTF-8");
            encodeCity = URLEncoder.encode(cnName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("Encode BaiduLocation Error");
            return ServiceResult.<BaiduMapLocation>builder().success(false).message("Error").build();
        }

        HttpClient httpClient = HttpClients.createDefault();
        StringBuilder sb = new StringBuilder(BAIDU_MAP_GEOONV_API);
        sb.append("address=").append(encodeAddress).append("&")
                .append("city=").append(encodeCity).append("&")
                .append("output=json&").append("ak=").append(BAIDU_MAP_KEY);

        HttpGet get = new HttpGet(sb.toString());

        try {
            HttpResponse response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                JsonNode jsonNode = objectMapper.readTree(result);
                int status = jsonNode.get("status").asInt();
                if (status == 0) {
                    BaiduMapLocation location = new BaiduMapLocation();
                    JsonNode locationNode = jsonNode.get("result").get("location");
                    location.setLongitude(locationNode.get("lng").asDouble());
                    location.setLatitude(locationNode.get("lat").asDouble());
                    return ServiceResult.<BaiduMapLocation>builder().success(true).message("success").result(location).build();
                }
            }
        } catch (Exception e) {
            log.error("Request Error");
            return ServiceResult.<BaiduMapLocation>builder().success(false).message("Error").build();
        }

        return ServiceResult.<BaiduMapLocation>builder().success(false).message("Error").build();
    }

    public ServiceResult lbsUpload(BaiduMapLocation location, String title, String address, Integer houseId, Integer price, Integer area) {
        CloseableHttpClient client = HttpClients.createDefault();
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("latitude", String.valueOf(location.getLatitude())));
        params.add(new BasicNameValuePair("longitude", String.valueOf(location.getLongitude())));
        params.add(new BasicNameValuePair("coord_type", "3"));
        params.add(new BasicNameValuePair("geotable_id", "xxx"));
        params.add(new BasicNameValuePair("ak", BAIDU_MAP_KEY));
        params.add(new BasicNameValuePair("houseId", String.valueOf(houseId)));
        params.add(new BasicNameValuePair("price", String.valueOf(price)));
        params.add(new BasicNameValuePair("area", String.valueOf(area)));
        params.add(new BasicNameValuePair("title", title));
        params.add(new BasicNameValuePair("address", address));

        HttpPost post;
        if (isLbsDataExists(houseId)) {
            post = new HttpPost(LBS_UPDATE_API);
        } else {
            post = new HttpPost(LBS_CREATE_API);
        }

        try {
            post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            CloseableHttpResponse response = client.execute(post);
            String resultResponse = EntityUtils.toString(response.getEntity());

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                JsonNode jsonNode = objectMapper.readTree(resultResponse);
                if (jsonNode.get("status").asInt() == 0) {
                    return ServiceResult.builder().success(true).message("success").build();
                }
            }
        } catch (Exception e) {
            log.error("Error");
            return ServiceResult.builder().success(false).message("Error").build();
        }
        return ServiceResult.builder().success(false).message("Operation Error").build();
    }

    private boolean isLbsDataExists(Integer houseId) {
        CloseableHttpClient client = HttpClients.createDefault();
        String sb = LBS_QUERY_API +"geotable_id=xxxx&ak=" + BAIDU_MAP_KEY + "&houseId=" + houseId;
        HttpGet httpGet = new HttpGet(sb);
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            String resultResponse = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                JsonNode jsonNode = objectMapper.readTree(resultResponse);
                if (jsonNode.get("status").asInt() == 0) {
                    return jsonNode.get("size").asLong() > 0;
                }
            }
        } catch (Exception e) {
            log.error("Request Error");
            return false;
        }
        return false;
    }
}
