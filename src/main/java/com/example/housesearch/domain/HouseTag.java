package com.example.housesearch.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/***
 * 标签对象
 */
@Entity(name = "house_tag")
@Table(name = "house_tag")
@Data
@Builder
@ToString
public class HouseTag implements Serializable {

    /**主键*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**房源*/
    @Column(name = "house_id")
    private Integer houseId;

    /**标签名*/
    private String name;

    public HouseTag(Integer id, Integer houseId, String name) {
        this.id = id;
        this.houseId = houseId;
        this.name = name;
    }

    protected HouseTag() {
    }
}
