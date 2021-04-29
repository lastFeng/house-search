package com.example.housesearch.controller;

import com.example.housesearch.domain.base.ApiResponse;
import com.example.housesearch.domain.base.ServiceResult;
import com.example.housesearch.service.HouseSubscribeService;
import com.example.housesearch.service.UserService;
import com.example.housesearch.utils.LoginUtils;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Objects;

/**
 * @author : guoweifeng
 * @date : 2021/4/28
 */
@Controller
@RequestMapping("/api/user")
public class ApiUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private HouseSubscribeService houseSubscribeService;

    /**
     * 修改用户名称|邮箱|密码
     */
    @PostMapping("/info")
    @ResponseBody
    public ApiResponse updateUserInfo(@RequestParam("profile") String profile, @RequestParam("value")String value) {
        if (Strings.isBlank(value)) {
            return ApiResponse.of(ApiResponse.Status.BAD_REQUEST.getCode(), ApiResponse.Status.BAD_REQUEST.getStandardMessage());
        }

        if ("emali".equals(profile) && !LoginUtils.checkEmail(value)) {
            return ApiResponse.of(HttpStatus.SC_BAD_REQUEST, "不支持的邮箱格式");
        }

        ServiceResult result = userService.modifyUserProfile(profile, value);
        ApiResponse response;
        if (result.isSuccess()) {
            response = ApiResponse.of(HttpStatus.SC_OK, "修改成功");
        } else {
            response = ApiResponse.of(HttpStatus.SC_BAD_REQUEST, result.getMessage());
        }
        return response;
    }

    /***
     * 获取订阅房源列表
     * TODO
     */
    @GetMapping("/house/subscribe/list")
    @ResponseBody
    public ApiResponse subscribeList(@RequestParam(value = "start", defaultValue = "0") int start,
                                     @RequestParam(value = "size", defaultValue = "10") int size,
                                     @RequestParam(value = "status") int status) {
        return null;
    }

    /***
     * 待订阅房源
     */
    @PostMapping("/house/subscribe")
    @ResponseBody
    public ApiResponse subscribeHouse(@RequestParam("houseId") Integer houseId) {
        ServiceResult result = houseSubscribeService.addSubscribeOrder(houseId);
        if (result.isSuccess()) {
            return ApiResponse.of(HttpStatus.SC_OK, "添加订阅成功");
        }
        return ApiResponse.of(HttpStatus.SC_BAD_REQUEST, result.getMessage());
    }

    /***
     * 订阅房源
     */
    @PostMapping("/house/subscribe/date")
    @ResponseBody
    public ApiResponse subscribeDate(@RequestParam("houseId") Integer houseId,
                                     @RequestParam("orderTime") @DateTimeFormat(pattern = "yyyy-MM-dd")Date orderTime,
                                     @RequestParam(value = "desc", required = false) String desc,
                                     @RequestParam("telephone") String telephone) {
        if (Objects.isNull(orderTime)) {
            return ApiResponse.of(HttpStatus.SC_BAD_REQUEST, "订阅时间不能为空");
        }
        if (Strings.isBlank(telephone)) {
            return ApiResponse.of(HttpStatus.SC_BAD_REQUEST, "手机号不能为空");
        }
        if (!LoginUtils.checkTelephone(telephone)) {
            return ApiResponse.of(HttpStatus.SC_BAD_REQUEST, "手机号格式不正确");
        }

        ServiceResult result = houseSubscribeService.subscribe(houseId, orderTime, desc, telephone);
        if (result.isSuccess()) {
            return ApiResponse.of(HttpStatus.SC_OK, "订阅成功");
        }
        return ApiResponse.of(HttpStatus.SC_INTERNAL_SERVER_ERROR, result.getMessage());
    }

    /**
     * 删除房源订阅内容
     */
    @DeleteMapping("/house/subscribe")
    @ResponseBody
    public ApiResponse cancelSubscribe(@RequestParam("houseId") Integer houseId) {
        ServiceResult result = houseSubscribeService.cancelSubscribe(houseId);
        if (result.isSuccess()) {
            return ApiResponse.of(HttpStatus.SC_OK, "取消订阅成功");
        }
        return ApiResponse.of(HttpStatus.SC_INTERNAL_SERVER_ERROR, result.getMessage());
    }
}
