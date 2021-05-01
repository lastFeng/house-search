package com.example.housesearch.domain.search;

import com.google.common.collect.ImmutableMap;
import com.sun.org.apache.regexp.internal.RE;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * @author : guoweifeng
 * @date : 2021/5/1
 * 区间的常用数值定义
 */
@AllArgsConstructor
@Data
public class RentValueBlock {

    /**
     * 价格区间
     */
    public static final Map<String, RentValueBlock> PRICE_BLOCK;

    /**
     * 面积区间
     */
    public static final Map<String, RentValueBlock> AREA_BLOCK;

    /**
     * 无限制区间
     */
    public static final RentValueBlock ALL = new RentValueBlock("*", -1, -1);

    /***
     *
     */
    static {
        PRICE_BLOCK = ImmutableMap.<String, RentValueBlock>builder()
                .put("*-1000", new RentValueBlock("*-1000", -1, 1000)).
                        put("1000-3000", new RentValueBlock("1000-3000", 1000, 3000)).
                        put("3000-*", new RentValueBlock("3000-*", 3000, -1)).build();

        AREA_BLOCK = ImmutableMap.<String, RentValueBlock>builder()
                .put("*-30", new RentValueBlock("*-30", -1, 30)).
                        put("30-50", new RentValueBlock("30-50", 30, 50)).
                        put("50-*", new RentValueBlock("50-*", 50, -1)).build();
    }

    private String key;
    private Integer min;
    private Integer max;

    public static RentValueBlock matchPrice(String key) {
        return PRICE_BLOCK.getOrDefault(key, ALL);
    }

    public static RentValueBlock matchArea(String key) {
        return AREA_BLOCK.getOrDefault(key, ALL);
    }
}
