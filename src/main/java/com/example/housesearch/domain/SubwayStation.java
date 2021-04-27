package com.example.housesearch.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/***
 * 地铁站信息表
 */
@Entity(name = "subway_station")
@Table(name = "subway_station")
@Data
@Builder
@ToString
public class SubwayStation implements Serializable {

    /**主键*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**地铁站名*/
    private String name;

    /**所属地铁线*/
    @Column(name = "subway_id")
    private Integer subwayId;

    public SubwayStation(Integer id, String name, Integer subwayId) {
        this.id = id;
        this.name = name;
        this.subwayId = subwayId;
    }

    protected SubwayStation() {
    }
}