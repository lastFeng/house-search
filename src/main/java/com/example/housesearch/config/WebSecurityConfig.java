package com.example.housesearch.config;

import com.example.housesearch.config.security.CustomAuthProvider;
import com.example.housesearch.config.security.CustomAuthFilter;
import com.example.housesearch.config.security.LoginAuthFailHandler;
import com.example.housesearch.config.security.LoginUrlEntryPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author : guoweifeng
 * @date : 2021/4/27
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableRedisHttpSession
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 增加用户名密码过滤器
        http.addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class);
        // 设置访问权限
        http.authorizeRequests()
                .antMatchers("/admin/login", "/static/**", "/user/login").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**", "/api/user/**").hasAnyRole("ADMIN", "USER")
                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                .failureHandler(authFailHandler())
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(urlEntryPoint())
                .accessDeniedPage("/403");

        http.csrf().disable();
        http.headers().frameOptions().sameOrigin();
    }

    /***
     * 自定义认证策略
     */
    @Autowired
    public void configGlobal(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider()).eraseCredentials(true);
    }

    /***
     * 设置认证提供器
     * @return
     */
    @Bean
    public CustomAuthProvider authProvider() {
        return new CustomAuthProvider();
    }

    /***
     * 设置登录点
     * @return
     */
    @Bean
    public LoginUrlEntryPoint urlEntryPoint() {
        return new LoginUrlEntryPoint("/user/login");
    }

    /***
     * 设置认证失败处理器
     * @return
     */
    @Bean
    public LoginAuthFailHandler authFailHandler() {
        return new LoginAuthFailHandler(urlEntryPoint());
    }

    /***
     * 设置认证拦截器
     * @return
     */
    @Bean
    public CustomAuthFilter authFilter() {
        CustomAuthFilter authFilter = new CustomAuthFilter();
        authFilter.setAuthenticationManager(authenticationManager());
        authFilter.setAuthenticationFailureHandler(authFailHandler());
        return authFilter;
    }

    /**
     * 设置认证管理器
     * @return
     */
    @Bean
    @Override
    protected AuthenticationManager authenticationManager() {
        try {
            return super.authenticationManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
