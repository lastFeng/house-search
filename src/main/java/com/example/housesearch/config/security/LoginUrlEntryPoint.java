package com.example.housesearch.config.security;

import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

/**
 * @author : guoweifeng
 * @date : 2021/4/27
 */
public class LoginUrlEntryPoint extends LoginUrlAuthenticationEntryPoint {

    /**
     * TODO
     * @param loginFormUrl
     */
    public LoginUrlEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }
}
