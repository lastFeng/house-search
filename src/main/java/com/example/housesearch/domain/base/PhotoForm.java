package com.example.housesearch.domain.base;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : guoweifeng
 * @date : 2021/4/28
 */
@Data
public class PhotoForm implements Serializable {
    private String path;
    private Integer width;
    private Integer height;
}
