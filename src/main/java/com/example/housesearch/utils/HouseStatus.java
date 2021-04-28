package com.example.housesearch.utils;

/**
 * @author : guoweifeng
 * @date : 2021/4/29
 */
public enum HouseStatus {
    /**未审核*/
    NOT_AUDITED(0),
    /**审核通过*/
    PASS(1),
    /**已租*/
    RENTED(2),
    /**已删除*/
    DELETED(3);

    private int value;

    HouseStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
