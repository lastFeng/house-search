package com.example.housesearch.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/***
 * 房源图片对象
 */
@Entity(name = "house_picture")
@Table(name = "house_picture")
@NoArgsConstructor
@Data
@Builder
@ToString
public class HousePicture implements Serializable {
    /**主键*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**房源*/
    @Column(name = "house_id")
    private Long houseId;

    /**cdn前缀*/
    @Column(name = "cdn_prefix")
    private String cdnPrefix;

    /**图片宽度*/
    private Integer width;

    /**图片高度*/
    private Integer height;

    /**图片位置*/
    private String location;

    /**图片路径*/
    private String path;
}
