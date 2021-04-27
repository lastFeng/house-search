package com.example.housesearch.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/***
 * 房源描述对象
 */
@Entity(name = "house_detail")
@Table(name = "house_detail")
@Data
@Builder
@ToString
public class HouseDetail implements Serializable {
    /**主键*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**房源描述*/
    private String description;

    /**布局描述*/
    @Column(name = "layout_desc")
    private String layoutDesc;

    /**交通*/
    private String traffic;

    /**周边设施*/
    @Column(name = "round_service")
    private String roundService;

    /**租赁方式*/
    @Column(name = "rent_way")
    private Integer rentWay;

    /**地址*/
    private String address;

    /**地铁线*/
    @Column(name = "subway_line_id")
    private Integer subwayLineId;

    /**地铁线名称*/
    @Column(name = "subway_line_name")
    private String subwayLineName;

    /**地铁站*/
    @Column(name = "subway_station_id")
    private Integer subwayStationId;

    /**地铁站名*/
    @Column(name = "subway_station_name")
    private String subwayStationName;

    /**房源*/
    @Column(name = "house_id")
    private Integer houseId;

    public HouseDetail(Integer id, String description, String layoutDesc, String traffic, String roundService,
                       Integer rentWay, String address, Integer subwayLineId, String subwayLineName,
                       Integer subwayStationId, String subwayStationName, Integer houseId) {
        this.id = id;
        this.description = description;
        this.layoutDesc = layoutDesc;
        this.traffic = traffic;
        this.roundService = roundService;
        this.rentWay = rentWay;
        this.address = address;
        this.subwayLineId = subwayLineId;
        this.subwayLineName = subwayLineName;
        this.subwayStationId = subwayStationId;
        this.subwayStationName = subwayStationName;
        this.houseId = houseId;
    }

    protected HouseDetail() {
    }
}
