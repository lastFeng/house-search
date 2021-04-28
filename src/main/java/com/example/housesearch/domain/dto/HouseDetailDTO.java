package com.example.housesearch.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : guoweifeng
 * @date : 2021/4/28
 */
@Data
public class HouseDetailDTO implements Serializable {
    private String description;
    private String layoutDesc;
    private String traffic;
    private String roundService;
    private Integer rentWay;
    private Integer adminId;
    private String address;
    private Integer subwayLineId;
    private Integer subwayStationId;
    private String subwayLineName;
    private String subwayStationName;
}
