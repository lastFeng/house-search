package com.example.housesearch.domain.dto;

import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : guoweifeng
 * @date : 2021/4/28
 */
@Data
public class HouseDTO implements Serializable {
    private Integer id;
    private String title;
    private Integer price;
    private Integer area;
    private Integer direction;
    private Integer room;
    private Integer parlour;
    private Integer bathroom;
    private Integer floor;
    private Integer adminId;
    private String district;
    private Integer totalFloor;
    private Integer watchTimes;
    private Integer buildYear;
    private Integer status;
    private Date createTime;
    private Date lastUpdateTime;
    private String cityEnName;
    private String regionEnName;
    private String street;
    private String cover;
    private Integer distanceToSubway;
    @Transient
    private HouseDetailDTO houseDetail;
    @Transient
    private List<HousePictureDTO> pictures;
    private List<String> tags;
    private Integer subscribeStatus;

    public List<String> getTags() {
        if (this.tags == null) {
            tags = new ArrayList<>();
        }
        return tags;
    }
}
