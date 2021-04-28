package com.example.housesearch.domain.base;

import lombok.Builder;
import lombok.Data;

/**
 * @author : guoweifeng
 * @date : 2021/4/28
 */
@Data
@Builder
public class ServiceResult<T> {
    private boolean success;
    private String message;
    private T result;
}
