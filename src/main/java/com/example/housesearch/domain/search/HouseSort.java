package com.example.housesearch.domain.search;

import com.google.common.collect.Sets;
import org.springframework.data.domain.Sort;

import java.util.Set;

import static com.example.housesearch.domain.search.HouseIndexKey.*;

/**
 * @author : guoweifeng
 * @date : 2021/5/1
 * 排序生成器
 */
public class HouseSort {
    private HouseSort(){}

    private static final String DEFAULT_SORT_KEY = LAST_UPDATE_TIME;
    private static final Set<String> SORT_KEYS = Sets.newHashSet(DEFAULT_SORT_KEY, CREATE_TIME, PRICE,
            AREA, DISTANCE_TO_SUBWAY);

    public static Sort generateSort(String key, String directionKey) {
        key = getSortKey(key);
        Sort.Direction direction = Sort.Direction.fromString(directionKey);
        return Sort.by(direction, key);
    }

    public static String getSortKey(String key) {
        if (!SORT_KEYS.contains(key)) {
            return DEFAULT_SORT_KEY;
        }
        return key;
    }
}
