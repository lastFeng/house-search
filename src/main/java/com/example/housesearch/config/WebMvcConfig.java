package com.example.housesearch.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author : guoweifeng
 * @date : 2021/4/27
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /***
     * 资源映射本地位置
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/**");
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
