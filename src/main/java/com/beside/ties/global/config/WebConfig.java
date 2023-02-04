package com.beside.ties.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE")
                .allowedOrigins("https://www.moyorafriends.co.kr/")
                .allowedOrigins("http://localhost:3000")
                .allowedOrigins("https://localhost:3000");
    }
}
