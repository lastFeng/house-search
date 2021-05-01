package com.example.housesearch.domain.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : guoweifeng
 * @date : 2021/5/1
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class HouseBucketDTO {
    /**
     * 聚合bucket的key
     */
    private String key;

    /**
     * 聚合结果值
     */
    private Long count;
}
