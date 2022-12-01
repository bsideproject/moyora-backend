package com.beside.ties.auth.filter;


import com.beside.ties.auth.kakao.KakaoUser;
import com.beside.ties.common.RequestUtil;
import com.beside.ties.common.exception.custom.InvalidSocialTokenException;
import org.springframework.http.HttpStatus;
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

public class KakaoCheckTokenFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = RequestUtil.getAuthorizationToken(request.getHeader("Authorization"));
        HttpURLConnection con = httpURLConnection(header, USER_INFO_URI);
        int responseCode = con.getResponseCode();
        if(responseCode == HttpStatus.OK.value()){
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = "";
            while((line = br.readLine()) != null){
                sb.append(line);
            }
            Gson gson = new Gson();
            KakaoUser kakaoUser = gson.fromJson(sb.toString(), KakaoUser.class);
            /**
             * kakakoUser 정보를 통해서 유저가 등록된 유저일 경우 토큰을 반환 아닐 경우 회원가입을 진행한다.
             */


        }
        else{
            throw new InvalidSocialTokenException();
        }

        filterChain.doFilter(request, response);
    }

    private HttpURLConnection httpURLConnection(String AccessToken, String requestUrl) {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Authorization", "Bearer " + AccessToken);
            return con;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
