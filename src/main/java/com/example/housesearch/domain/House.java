package com.example.housesearch.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/***
 * 房源信息对象
 */
@Entity(name = "house")
@Table(name = "house")
@Data
@Builder
@ToString
public class House implements Serializable {
    /**主键*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**标题*/
    private String title;

    /**价格*/
    private Integer price;
    /**面积*/
    private Integer area;

    /**房间号*/
    private Integer room;

    /**楼层*/
    private Integer floor;

    /**总楼层*/
    @Column(name = "total_floor")
    private Integer totalFloor;

    /**查看次数*/
    @Column(name = "watch_times")
    private Integer watchTimes;

    /**建房年份*/
    @Column(name = "build_year")
    private Integer buildYear;

    /**房源状态*/
    private Integer status;

    /**创建时间*/
    @Column(name = "create_time")
    @JsonFormat(timezone = "GMT+*", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**更新时间*/
    @Column(name = "last_update_time")
    @JsonFormat(timezone = "GMT+*", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;

    /**所属城市*/
    @Column(name = "city_en_name")
    private String cityEnName;

    /**所属区域*/
    @Column(name = "region_en_name")
    private String regionEnName;

    /**封面*/
    private String cover;

    /**朝向*/
    private Integer direction;

    /**邻近地铁状态*/
    @Column(name = "distance_to_subway")
    private Integer distanceToSubway;

    /**客厅数*/
    private Integer parlour;

    /**小区*/
    private String district;

    /**管理员*/
    @Column(name = "admin_id")
    private Integer adminId;

    /**浴室数*/
    private Integer bathroom;

    /**街道*/
    private String street;

    public House(Integer id, String title, Integer price, Integer area, Integer room, Integer floor,
                 Integer totalFloor, Integer watchTimes, Integer buildYear, Integer status, Date createTime,
                 Date lastUpdateTime, String cityEnName, String regionEnName, String cover, Integer direction,
                 Integer distanceToSubway, Integer parlour, String district, Integer adminId, Integer bathroom, String street) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.area = area;
        this.room = room;
        this.floor = floor;
        this.totalFloor = totalFloor;
        this.watchTimes = watchTimes;
        this.buildYear = buildYear;
        this.status = status;
        this.createTime = createTime;
        this.lastUpdateTime = lastUpdateTime;
        this.cityEnName = cityEnName;
        this.regionEnName = regionEnName;
        this.cover = cover;
        this.direction = direction;
        this.distanceToSubway = distanceToSubway;
        this.parlour = parlour;
        this.district = district;
        this.adminId = adminId;
        this.bathroom = bathroom;
        this.street = street;
    }

    protected House() {
    }
}
