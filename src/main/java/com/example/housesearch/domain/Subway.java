package com.example.housesearch.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/***
 * 地铁线表
 */
@Entity(name = "subway")
@Table(name = "subway")
@Data
@ToString
public class Subway implements Serializable {

    /**主键*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**地铁线名*/
    private String name;

    /**地铁所属城市*/
    @Column(name = "city_en_name")
    private String cityEnName;
}
