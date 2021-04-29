package com.example.housesearch.utils;

/**
 * @author : guoweifeng
 * @date : 2021/4/29
 */
public interface Contant {
    /**权限前缀*/
    String AUTHORITY_PREFIX = "ROLE_";
    /**用户角色名称*/
    String USER_ROLE_NAME = "USER";

    // 房源订阅相关状态
    /**未订阅*/
    Integer SUBSCRIBE_STATUS_NO_SUBSCRIBE = 0;
    /**已在订阅列表中*/
    Integer SUBSCRIBE_STATUS_IN_ORDER_LIST = 1;
    /**已预约时间*/
    Integer SUBSCRIBE_STATUS_IN_ORDER_TIME = 2;
    /**完成*/
    Integer SUBSCRIBE_STATUS_FINISH = 3;
}
