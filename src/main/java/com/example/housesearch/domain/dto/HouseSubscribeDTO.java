package com.example.housesearch.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : guoweifeng
 * @date : 2021/4/28
 */
@Data
public class HouseSubscribeDTO implements Serializable {
    private Integer id;
    private Integer houseId;
    private Integer userId;
    private Integer adminId;
    // 预约状态 1-加入待看清单 2-已预约看房时间 3-看房完成
    private Integer status;
    private Date createTime;
    private Date lastUpdateTime;
    private Date orderTime;
    private String telephone;
    private String desc;
}
