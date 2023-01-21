package com.beside.ties.global.config;

import com.beside.ties.domain.account.service.AccountService;
import com.beside.ties.global.auth.filter.JwtAuthorizationFilter;
import com.beside.ties.global.auth.filter.KakaoAuthorizationFilter;
import com.beside.ties.global.auth.filter.MockJwtFilter;
import com.beside.ties.global.auth.security.AuthFilterContainer;
import com.beside.ties.global.auth.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@RequiredArgsConstructor
public class AuthConfig {
    private final static Logger log = LoggerFactory.getLogger(AuthConfig.class);
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final AccountService accountService;


    @Bean
    @Profile({"test","h2"})
    public AuthFilterContainer mockAuthFilter() {
        log.info("Initializing local Filter");
        AuthFilterContainer authFilterContainer = new AuthFilterContainer();
        authFilterContainer.setAuthFilter(new MockJwtFilter(accountService));
        return authFilterContainer;
    }

    @Bean
    @Profile({"prod","default"})
    public AuthFilterContainer JWTAuthFilter() {
        log.info("Initializing JWT Filter");
        AuthFilterContainer authFilterContainer = new AuthFilterContainer();
        authFilterContainer.setAuthFilter(new JwtAuthorizationFilter(jwtUtil,userDetailsService ));
        return authFilterContainer;
    }
}

