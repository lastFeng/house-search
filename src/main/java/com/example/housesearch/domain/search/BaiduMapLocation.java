package com.example.housesearch.domain.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author : guoweifeng
 * @date : 2021/5/1
 * 百度地图位置信息
 */
@Data
public class BaiduMapLocation {
    @JsonProperty("lon")
    private Double longitude;

    @JsonProperty("lat")
    private Double latitude;

}
