package com.beside.ties.auth.filter;


import com.beside.ties.auth.kakao.KakaoUser;
import com.beside.ties.common.RequestUtil;
import com.beside.ties.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Component
public class KakaoAuthorizationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    private final AccountService accountService;

    private final List<String> WHITE_LIST_EQUALS = Arrays.asList("/");
    private final List<String> WHITE_LIST_STARTS = Arrays.asList("/api/v1/login","/swagger","/v3","/h2-console");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        if(checkWhiteList(request, response, filterChain) == false) {
            String token = RequestUtil.getAuthorizationToken(request.getHeader("Authorization"));
            UserDetails account = null;
            KakaoUser kakaoUser = accountService.getUserFromToken(token);

            // kakakoUser 정보를 통해서 유저가 등록된 유저일 경우 토큰을 반환 아닐 경우 회원가입을 진행한다.
            try {
                account = userDetailsService.loadUserByUsername(kakaoUser.getId());
            } catch (UsernameNotFoundException e) {
                accountService.register(kakaoUser);
                account = userDetailsService.loadUserByUsername(kakaoUser.getId());
            } finally {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        account, null, account.getAuthorities());//인증 객체 생성
                SecurityContextHolder.getContext().setAuthentication(authentication);//securityContextHolder 에 인증 객체 저장
            }
        }

        filterChain.doFilter(request, response);

    }

    private boolean checkWhiteList(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        boolean result = false;

        if(WHITE_LIST_EQUALS.contains(request.getServletPath()))
            result = true;

        for(int i=0; i<WHITE_LIST_STARTS.size(); i++){
            if(request.getServletPath().startsWith(WHITE_LIST_STARTS.get(i)))
                result = true;
        }
        return result;
    }
}
