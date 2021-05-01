package com.example.housesearch.domain.search;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;
import java.util.List;

import static com.example.housesearch.utils.Constant.HOUSE_INDEX_NAME;

/**
 * @author : guoweifeng
 * @date : 2021/5/1
 */
@Data
@Document(indexName = HOUSE_INDEX_NAME)
public class HouseIndex {
    private Integer houseId;
    private String title;
    private int price;
    private int area;
    private Date createTime;
    private Date lastUpdateTime;
    private String cityEnName;
    private String regionEnName;
    private int direction;
    private int distanceToSubway;
    private String subwayLineName;
    private String subwayStationName;
    private String street;
    private String district;
    private String description;
    private String layoutDesc;
    private String traffic;
    private String roundService;
    private int rentWay;
    private List<String> tags;
    private List<HouseSuggest> suggest;
    private BaiduMapLocation location;
}
