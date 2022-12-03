package com.beside.ties.auth.filter;


import com.beside.ties.auth.kakao.KakaoUser;
import com.beside.ties.common.RequestUtil;
import com.beside.ties.common.exception.custom.InvalidSocialTokenException;
import com.beside.ties.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;
import com.google.gson.Gson;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

import static com.beside.ties.auth.kakao.KakaoOAuthConstants.USER_INFO_URI;

@RequiredArgsConstructor
public class KakaoAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private final UserDetailsService userDetailsService;

    @Autowired
    private final UsersService usersService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = RequestUtil.getAuthorizationToken(request.getHeader("Authorization"));
        UserDetails user = null;
        KakaoUser kakaoUser = usersService.getUserFromToken(token);

        // kakakoUser 정보를 통해서 유저가 등록된 유저일 경우 토큰을 반환 아닐 경우 회원가입을 진행한다.
        try {
            user = userDetailsService.loadUserByUsername(kakaoUser.getKakaoAccount().getEmail());
        }catch (UsernameNotFoundException e){
            usersService.register(kakaoUser);
        }finally {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities());//인증 객체 생성
            SecurityContextHolder.getContext().setAuthentication(authentication);//securityContextHolder 에 인증 객체 저장
        }

        filterChain.doFilter(request, response);

    }
}
