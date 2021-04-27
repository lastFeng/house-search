package com.example.housesearch.config.security;

import com.example.housesearch.domain.User;
import com.example.housesearch.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;

/**
 * 自定义认证器
 * 使用密码加密算法，将输入的密码与数据库进行匹配认证
 * @author : guoweifeng
 * @date : 2021/4/27
 */
@Slf4j
public class CustomAuthProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    /***
     * 获取输入的密码 + 用户名
     * 通过用户名查询用户，判断密码
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String inputPassword = authentication.getPrincipal().toString();
        String username = authentication.getName();

        User user = userService.findUserByName(username);
        if (Objects.isNull(user) || !bCryptPasswordEncoder.matches(inputPassword, user.getPassword())) {
            log.error("用户名或密码输入错误: " + username + " ： " + inputPassword);
            throw new AuthenticationServiceException("用户名或密码输入错误");
        }

        // 用户名登录成功，获取Token
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
