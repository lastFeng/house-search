package com.example.housesearch.config.security;

import com.example.housesearch.domain.User;
import com.example.housesearch.service.SmsService;
import com.example.housesearch.service.UserService;
import com.example.housesearch.utils.LoginUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author : guoweifeng
 * @date : 2021/4/27
 * 由于使用了：手机 + 用户 的两种登录方式
 *   手机：通过对应的字段，进行设置 （首次登录 与 已有账户的逻辑）
 *   用户：通过用户名登录
 *   自定义过滤器
 */
@Slf4j
public class CustomAuthFilter extends UsernamePasswordAuthenticationFilter {
    /***
     * 用户服务
     */
    @Autowired
    private UserService userService;

    /***
     * 手机验证码服务
     */
    @Autowired
    private SmsService smsService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 先尝试获取用户名
        String username = obtainUsername(request);
        if (!StringUtils.isEmpty(username)) {
            return super.attemptAuthentication(request, response);
        }

        // 然后是手机验证码:获取手机号
        String telephone = request.getParameter("telephone");
        if (StringUtils.isEmpty(telephone) || LoginUtils.checkErrorTelephone(telephone)) {
            log.error("Wrong telephone number: " + telephone);
            throw new BadCredentialsException("Wrong telephone number!");
        }

        User user = userService.findUserByTelephone(telephone);
        String smsCode = request.getParameter("smsCode");
        String code = smsService.getSmsCode(telephone);
        if (Objects.equals(smsCode, code)) {
            // 是否是第一次使用手机号
            // 手机号登录不需要通过认证器了，直接获取对应Token
            if (Objects.isNull(user)) {
                user = userService.addUserByPhone(telephone);
            }
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        } else {
            log.error("smsCodeError: " + smsCode);
            throw new BadCredentialsException("smsCodeError!");
        }
    }
}
