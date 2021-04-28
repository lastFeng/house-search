package com.example.housesearch.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author : guoweifeng
 * @date : 2021/4/28
 */
@Data
public class HousePictureDTO implements Serializable {
    private Integer id;
    @JsonProperty(value = "house_id")
    private Integer houseId;
    private String path;
    @JsonProperty(value = "cdn_prefix")
    private String cdnPrefix;
    private Integer width;
    private Integer height;
}
