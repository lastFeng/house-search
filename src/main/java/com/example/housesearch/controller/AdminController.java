package com.example.housesearch.controller;

import com.example.housesearch.domain.base.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author : guoweifeng
 * @date : 2021/4/27
 */
@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Value("${spring.servlet.multipart.location}")
    private String localPath;

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

    @PostMapping(value = "/upload/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ApiResponse uploadPhoto(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ApiResponse.of(501, "NOT_INVALID_PARAMS");
        }
        String fileName = file.getOriginalFilename();
        File target = new File(localPath + fileName);
        try {
            file.transferTo(target);
        } catch (IOException e) {
            log.error("文件上传失败：" + e.getMessage());
            return ApiResponse.of(500, e.getMessage());
        }

        return ApiResponse.of(200);
    }
}
