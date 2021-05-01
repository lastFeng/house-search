package com.example.housesearch.domain.search;

import lombok.Data;

/**
 * @author : guoweifeng
 * @date : 2021/5/1
 */
@Data
public class HouseSuggest {
    /**输入内容*/
    private String input;
    /**权重*/
    private int weight = 10;
}
