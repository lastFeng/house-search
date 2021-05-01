package com.example.housesearch.controller;

import com.example.housesearch.domain.base.ApiResponse;
import com.example.housesearch.domain.base.ServiceResult;
import com.example.housesearch.service.SmsService;
import com.example.housesearch.utils.LoginUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author : guoweifeng
 * @date : 2021/4/28
 */
@Controller
public class IndexController {
    @Autowired
    private SmsService smsService;

    /***
     * 首页
     * @return
     */
    @GetMapping("/index")
    public String index() {
        return "/index";
    }

    /**
     * 403页面
     * @return
     */
    @GetMapping("403")
    public String accessErrorPage() {
        return "403";
    }

    /**
     * 404页面
     * @return
     */
    @GetMapping("404")
    public String notFoundPage() {
        return "404";
    }

    /**
     * 500页面
     * @return
     */
    @GetMapping("500")
    public String serverInternalError() {
        return "505";
    }

    /**
     * 登出页面
     * @return
     */
    @GetMapping("logout")
    public String logout() {
        return "logout";
    }

    /**
     * 获取短信验证码页面
     */
    @GetMapping("/sms/code")
    @ResponseBody
    public ApiResponse getSmsCode(@RequestParam("telephone") String telephone) {
        if (LoginUtils.checkTelephone(telephone)) {
            ServiceResult<String> smsCode = smsService.sendSms(telephone);
            if (smsCode.isSuccess()) {
                return ApiResponse.of(HttpStatus.SC_OK, smsCode.getMessage(), smsCode.getResult());
            }
            return ApiResponse.of(HttpStatus.SC_BAD_REQUEST, smsCode.getMessage());
        }

        return ApiResponse.of(HttpStatus.SC_BAD_REQUEST, "Error Telephone Format");
    }
}
