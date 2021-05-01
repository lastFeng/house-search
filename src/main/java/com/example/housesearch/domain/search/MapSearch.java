package com.example.housesearch.domain.search;

import lombok.Data;

/**
 * @author : guoweifeng
 * @date : 2021/5/1
 */
@Data
public class MapSearch {
    private String cityEnName;
    /**地图缩放级别*/
    private Integer level = 12;
    private String orderBy = HouseIndexKey.LAST_UPDATE_TIME;
    private String orderDirection = "desc";
    /**左上角*/
    private Double leftLongitude;
    private Double leftLatitude;
    /**右下角*/
    private Double rightLongitude;
    private Double rightLatitde;

    private int start = 0;
    private int size = 10;
}
