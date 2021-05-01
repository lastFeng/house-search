package com.example.housesearch.domain.search;

import lombok.Data;

/**
 * 租房请求参数信息
 * @author : guoweifeng
 * @date : 2021/5/1
 */
@Data
public class RentSearch {
    private String cityEnName;
    private String regionEnName;
    private String priceBlock;
    private String areaBlock;
    private Integer room;
    private Integer direction;
    private String keywords;
    private Integer rentWay = -1;
    private String orderBy = "lastUpdateTime";
    private String orderDirection = "desc";
    private Integer start = 0;
    private Integer size = 10;

    public Integer getRentWay() {
        if (rentWay > -2 && rentWay < 2) {
            return rentWay;
        }
        return -1;
    }

    public Integer getSize() {
        if (size < 1) {
            return 10;
        } else if (size > 100) {
            return 100;
        } else {
            return size;
        }
    }
}
