package com.example.housesearch.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author : guoweifeng
 * @date : 2021/4/30
 */
@Data
public class SupportAddressDTO {
    private Integer id;
    @JsonProperty(value = "belong_to")
    private String belongTo;
    @JsonProperty(value = "en_name")
    private String enName;
    @JsonProperty(value = "cn_name")
    private String cnName;
    private String level;
    private double baiduMapLongitude;
    private double baiduMapLatitude;
}
