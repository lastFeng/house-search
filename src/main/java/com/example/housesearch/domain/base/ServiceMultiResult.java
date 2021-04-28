package com.example.housesearch.domain.base;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author : guoweifeng
 * @date : 2021/4/28
 */
@Data
@Builder
public class ServiceMultiResult<T> {
    private Long total;
    private List<T> result;
}
