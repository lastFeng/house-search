package com.example.housesearch.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/***
 * 用户表
 */
@Entity(name = "user")
@Table(name = "user")
@Data
@Builder
@ToString
public class User implements Serializable {

    /**主键*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**用户名*/
    private String name;

    /**邮箱*/
    private String email;

    /**手机号*/
    @Column(name = "phone_number")
    private String phoneNumber;

    /**密码*/
    private String password;

    /**用户状态*/
    private Integer status;

    /**创建时间*/
    @Column(name = "create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**登录时间*/
    @Column(name = "last_login_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;

    /**更新时间*/
    @Column(name = "last_update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;

    /**用户头像*/
    private String avatar;

    public User(Integer id, String name, String email, String phoneNumber, String password, Integer status,
                Date createTime, Date lastLoginTime, Date lastUpdateTime, String avatar) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.status = status;
        this.createTime = createTime;
        this.lastLoginTime = lastLoginTime;
        this.lastUpdateTime = lastUpdateTime;
        this.avatar = avatar;
    }

    protected User() {
    }
}
