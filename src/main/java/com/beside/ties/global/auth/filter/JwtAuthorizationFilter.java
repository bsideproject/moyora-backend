package com.beside.ties.global.auth.filter;

import com.beside.ties.global.auth.security.jwt.JwtType;
import com.beside.ties.global.auth.security.jwt.JwtUtil;
import com.beside.ties.global.common.RequestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.beside.ties.global.auth.filter.WhiteList.checkWhiteList;

@RequiredArgsConstructor
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = null;

        if(!request.getServletPath().equals("/api/v1/user/signin")) {
            token = RequestUtil.getAuthorizationToken(request.getHeader("Authorization"));
        }
        if(token != null) {
            validateToken(token);
        }

        filterChain.doFilter(request, response);

    }

    private void validateToken(String token) {
        if(jwtUtil.getSubject(token) == JwtType.ACCESS_TOKEN)
        {
            String kakaoId = jwtUtil.getClaim(token, "kakaoId");
            UserDetails details = userDetailsService.loadUserByUsername(kakaoId);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    details, null, details.getAuthorities());//인증 객체 생성
            SecurityContextHolder.getContext().setAuthentication(authentication);//securityContextHolder 에 인증 객체 저장
        }
    }
}
