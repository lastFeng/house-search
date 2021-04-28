package com.example.housesearch.config.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : guoweifeng
 * @date : 2021/4/27
 */
public class LoginUrlEntryPoint extends LoginUrlAuthenticationEntryPoint {

    private static final String API_PREFIX = "/api";
    private static final String API_CODE_403 = "{\"code\": 403}";
    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    private PathMatcher pathMatcher = new AntPathMatcher();
    private final Map<String, String> authEntryPointMap;

    @Override
    protected String determineUrlToUseForThisRequest(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        // 获取地址, 只取部分数据
        String url = request.getRequestURI().replace(request.getContextPath(), "");
        // 判断是否是相应的地址，进行跳转到对应登录界面
        for (Map.Entry<String, String> stringStringEntry : authEntryPointMap.entrySet()) {
            if (pathMatcher.match(stringStringEntry.getKey(), url)) {
                return stringStringEntry.getValue();
            }
        }
        return super.determineUrlToUseForThisRequest(request, response, exception);
    }

    /***
     * 执行跳转到登录页面中
     * 判断是API接口的路径，那么就不进行跳转
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String uri = request.getRequestURI();
        if (uri.startsWith(API_PREFIX)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(CONTENT_TYPE);
            PrintWriter writer = response.getWriter();
            writer.write(API_CODE_403);
            writer.close();
        } else {
            super.commence(request, response, authException);
        }
    }

    /**
     * @param loginFormUrl
     */
    public LoginUrlEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
        authEntryPointMap = new ConcurrentHashMap<>();

        // 普通用户登录入口
        authEntryPointMap.put("/users/**", "/user/login");
        // 管理员用户登录入口
        authEntryPointMap.put("/admin/**", "/admin/login");
    }
}
