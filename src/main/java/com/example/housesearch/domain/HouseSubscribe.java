package com.example.housesearch.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/***
 * 房源预约对象
 */
@Entity(name = "house_subscribe")
@Table(name = "house_subscribe")
@Data
@Builder
@ToString
public class HouseSubscribe implements Serializable {

    /**主键*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**房源*/
    @Column(name = "house_id")
    private Integer houseId;

    /**用户*/
    @Column(name = "user_id")
    private Integer userId;

    /**描述*/
    private String desc;

    /**状态*/
    private Integer status;

    /**创建时间*/
    @Column(name = "create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**更新时间*/
    @Column(name = "last_update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;

    /**预约时间*/
    @Column(name = "order_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderTime;

    /**预约电话*/
    private String telephone;

    /**管理员*/
    @Column(name = "admin_id")
    private Integer adminId;

    public HouseSubscribe(Integer id, Integer houseId, Integer userId, String desc, Integer status,
                          Date createTime, Date lastUpdateTime, Date orderTime, String telephone, Integer adminId) {
        this.id = id;
        this.houseId = houseId;
        this.userId = userId;
        this.desc = desc;
        this.status = status;
        this.createTime = createTime;
        this.lastUpdateTime = lastUpdateTime;
        this.orderTime = orderTime;
        this.telephone = telephone;
        this.adminId = adminId;
    }

    protected HouseSubscribe() {
    }
}
