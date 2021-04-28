package com.example.housesearch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author : guoweifeng
 * @date : 2021/4/28
 */
@Controller
@RequestMapping("/user")
public class UserController {

    /***
     * 获取用户登录页面
     * @return
     */
    @GetMapping("/login")
    public String userLoginPage(){
        return "/user/login";
    }

    /***
     * 获取用户中心页面
     * @return
     */
    @GetMapping("/center")
    public String userCenter() {
        return "/user/center";
    }
}
