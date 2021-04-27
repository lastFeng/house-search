package com.example.housesearch.domain.base;

import lombok.Data;

import java.util.Objects;

/**
 * @author : guoweifeng
 * @date : 2021/4/27
 * API格式封装
 */
@Data
public class ApiResponse {
    private int code;
    private String message;
    private Object data;

    private ApiResponse(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ApiResponse of(int code) {
        return of(code, "", null);
    }

    public static ApiResponse of(int code, String message) {
        return of(code, message, null);
    }

    public static ApiResponse of(int code, String message, Object data) {
        return new ApiResponse(code, message, data);
    }

    /***
     *
     */
    public enum Status {
        /***
         * 200 - 成功
         */
        SUCCESS(200, "OK"),
        BAD_REQUEST(400, "Bad Request"),
        NOT_FOUNT(404, "Not Found"),
        INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
        NOT_LOGIN(50000, "Not Login"),
        NOT_AUTHENTICATION(50001, "Not Authentication"),
        NOT_PERMISSION(50002, "Not Permission");

        private int code;
        private String standardMessage;

        Status(int code, String standardMessage) {
            this.code = code;
            this.standardMessage = standardMessage;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getStandardMessage() {
            return standardMessage;
        }

        public void setStandardMessage(String standardMessage) {
            this.standardMessage = standardMessage;
        }
    }
}
