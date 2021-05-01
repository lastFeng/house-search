package com.example.housesearch.domain.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : guoweifeng
 * @date : 2021/5/1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HouseIndexMessage {
    public static final String INDEX = "index";
    public static final String REMOVE = "remove";
    public static final int MAX_RETRY = 3;
    private Integer houseId;
    private String operation;
    private int retry = 0;
}
