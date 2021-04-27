package com.example.housesearch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author : guoweifeng
 * @date : 2021/4/27
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/center")
    public  String adminCenter() {
        return "/admin/center";
    }

    @GetMapping("/welcome")
    public String adminWelcome() {
        return "/admin/welcome";
    }

    @GetMapping("/common")
    public String adminCommon() {
        return "/admin/common";
    }
}
