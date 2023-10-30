package com.connectravel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 리소스 추가 핸들러 : 정적 이미지를 외부로 빼서 바로 반영
    // 14 ~ 15행 : "/imgtest/**"로 시작하는 요청 > "file:./imgtest/" 매핑
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/imgtest/**")
                .addResourceLocations("file:./imgtest/");
    }
}