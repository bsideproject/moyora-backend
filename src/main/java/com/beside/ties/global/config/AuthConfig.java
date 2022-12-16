package com.beside.ties.global.config;

import com.beside.ties.global.auth.filter.KakaoAuthorizationFilter;
import com.beside.ties.global.auth.filter.MockJwtFilter;
import com.beside.ties.global.auth.security.AuthFilterContainer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
public class AuthConfig {

    private final KakaoAuthorizationFilter kakaoAuthorizationFilter;
    private final MockJwtFilter mockJwtFilter;
    private final static Logger log = LoggerFactory.getLogger(AuthConfig.class);


    @Bean
    @Profile({"test"})
    public AuthFilterContainer mockAuthFilter() {
        log.info("Initializing local Filter");
        AuthFilterContainer authFilterContainer = new AuthFilterContainer();
        authFilterContainer.setAuthFilter(kakaoAuthorizationFilter);
        return authFilterContainer;
    }

    @Bean
    @Profile({"prod","default"})
    public AuthFilterContainer firebaseAuthFilter() {
        log.info("Initializing kakao Filter");
        AuthFilterContainer authFilterContainer = new AuthFilterContainer();
        authFilterContainer.setAuthFilter(mockJwtFilter);
        return authFilterContainer;
    }
}

