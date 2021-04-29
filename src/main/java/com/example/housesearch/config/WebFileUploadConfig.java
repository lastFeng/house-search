package com.example.housesearch.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;

/**
 * 文件上传配置
 * 如果是SpringBoot的话是可以不用手动配，已经给配置了
 * @author : guoweifeng
 * @date : 2021/4/29
 */
//@Configuration
//@ConditionalOnClass({Servlet.class, StandardServletMultipartResolver.class, MultipartConfigElement.class})
//@ConditionalOnProperty(prefix = "spring.servlet.multipart", name = "enable", matchIfMissing = true)
//@EnableConfigurationProperties(MultipartProperties.class)
//public class WebFileUploadConfig {
//
//    @Autowired
//    private MultipartProperties multipartProperties;
//
//    /***
//     * 上传配置
//     * @return
//     */
//    @Bean
//    @ConditionalOnMissingBean
//    public MultipartConfigElement multipartConfigElement() {
//        return this.multipartProperties.createMultipartConfig();
//    }
//
//    /***
//     * 注册解析器
//     */
//    @Bean(name = DispatcherServlet.MULTIPART_RESOLVER_BEAN_NAME)
//    @ConditionalOnMissingBean(MultipartResolver.class)
//    public StandardServletMultipartResolver multipartResolver() {
//        StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();
//        multipartResolver.setResolveLazily(this.multipartProperties.isResolveLazily());
//        return multipartResolver;
//    }
//}
