package com.x2bee.api.common.base.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@PropertySource("classpath:config/application-${spring.profiles.active:local}.properties")
public class WebMvcConfig implements WebMvcConfigurer { 
}
