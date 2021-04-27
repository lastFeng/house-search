package com.example.housesearch.controller;

import com.example.housesearch.domain.base.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author : guoweifeng
 * @date : 2021/4/27
 */
@Controller
public class AppErrorController implements ErrorController {

    private final static String ERROR_PATH = "/error";
    private ErrorAttributes errorAttributes;

    @Autowired
    public AppErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    /**
     * 未找到任何页面，或者某些请求时，返回的Content-Type为：text/html时，进行拦截
     * @param response
     * @return
     */
    @GetMapping(name = ERROR_PATH, produces = "text/html")
    public String errorPageHandler(HttpServletResponse response) {
        int status = response.getStatus();
        switch (status) {
            case 403:
                return"403";
            case 404:
                return "404";
            case 500:
                return "500";
            default:
                break;
        }
        return "/index";
    }

    /***
     * 请求API，出错时拦截
     */
    @GetMapping(name = ERROR_PATH)
    public ApiResponse errorApiHandler(HttpServletRequest request) {
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(new ServletWebRequest(request), false);
        int status = getStatus(request);

        return ApiResponse.of(status, String.valueOf(errorAttributes.getOrDefault("message", "error")));
    }

    private int getStatus(HttpServletRequest request) {
        String parameter = request.getParameter(RequestDispatcher.ERROR_STATUS_CODE);
        if (parameter != null) {
            return Integer.parseInt(parameter);
        }
        return 500;
    }
}
