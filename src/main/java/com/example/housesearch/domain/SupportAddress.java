package com.example.housesearch.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/***
 * 支持城市表
 */
@Entity(name = "support_address")
@Table(name = "support_address")
@Data
@Builder
@ToString
public class SupportAddress implements Serializable {

    /**主键*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**所属城市*/
    @Column(name = "belong_to")
    private String belongTo;

    /**城市英文名*/
    @Column(name = "en_name")
    private String enName;

    /**城市中文名*/
    @Column(name = "cn_name")
    private String cnName;

    /**城市级别*/
    private String level;

    /**百度地图经度*/
    @Column(name = "baidu_map_lng")
    private Double baiduMapLng;

    /**百度地图维度*/
    @Column(name = "baidu_map_lat")
    private Double baiduMapLat;

    public SupportAddress(Integer id, String belongTo, String enName, String cnName,
                          String level, Double baiduMapLng, Double baiduMapLat) {
        this.id = id;
        this.belongTo = belongTo;
        this.enName = enName;
        this.cnName = cnName;
        this.level = level;
        this.baiduMapLng = baiduMapLng;
        this.baiduMapLat = baiduMapLat;
    }

    protected SupportAddress() {
    }
}
