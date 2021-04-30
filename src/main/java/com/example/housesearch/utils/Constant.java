package com.example.housesearch.utils;

/**
 * @author : guoweifeng
 * @date : 2021/4/29
 */
public interface Constant {
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

    // 房源审核操作
    /**通过审核*/
    int HOUSEOPERATION_PASS= 1;
    /**下架*/
    int HOUSEOPERATION_PULL_OUT = 2;
    /**逻辑删除*/
    int HOUSEOPERATION_DELETE = 3;
    /**出租*/
    int HOUSEOPERATION_RENT = 4;

    /**
     * 城市级别
     */
    public enum CityLevel {
        CITY("city"),
        REGION("region");

        private String value;

        CityLevel(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static CityLevel of(String value) {
            for (CityLevel level: CityLevel.values()) {
                if (level.getValue().equals(value)) {
                    return level;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum HouseStatus {
        /**
         * 未审核
         */
        NOT_AUDITED(0),
        /**
         * 审核通过
         */
        PASS(1),
        /**
         * 已租
         */
        RENTED(2),
        /**
         * 已删除
         */
        DELETED(3);

        private int value;

        HouseStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
