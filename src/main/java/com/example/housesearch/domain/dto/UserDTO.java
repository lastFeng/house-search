package com.example.housesearch.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : guoweifeng
 * @date : 2021/4/29
 */
@Data
public class UserDTO implements Serializable {
    private Integer id;
    private String name;
    private String avatar;
    private String phoneNumber;
    private String lastLoginTime;
}
