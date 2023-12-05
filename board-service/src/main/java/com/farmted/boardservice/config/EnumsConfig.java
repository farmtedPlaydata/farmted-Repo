package com.farmted.boardservice.config;

import com.farmted.boardservice.config.enumConfig.RoleEnumsConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class EnumsConfig implements WebMvcConfigurer {
    @Override
    // Role Enum 설정
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new RoleEnumsConverter());
    }
}