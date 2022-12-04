package com.beside.ties.service;

import com.beside.ties.auth.kakao.KakaoToken;
import com.beside.ties.auth.kakao.KakaoUser;
import com.beside.ties.common.exception.custom.InvalidSocialTokenException;
import com.beside.ties.domain.users.Users;
import com.beside.ties.domain.users.UsersRepo;
import com.beside.ties.dto.user.response.LoginResponseDto;
import com.beside.ties.dto.user.response.OAuthResponseDto;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.beside.ties.auth.kakao.KakaoOAuthConstants.USER_INFO_URI;

@RequiredArgsConstructor
@Transactional
@Service
public class UsersService {

    Logger logger = LoggerFactory.getLogger(UsersService.class);

    private final UsersRepo usersRepo;

    private final PasswordEncoder passwordEncoder;

    public Long register(KakaoUser kakaoUser){
        Users users = Users.toUserFromKakao(kakaoUser);
        users.UpdatePassword(passwordEncoder.encode(users.getPw()));
        Users save = usersRepo.save(users);

        if(save == null) throw new IllegalArgumentException("유저 저장 실패");
        logger.debug("유저 저장 성공");

        return save.getId();

    }

    public OAuthResponseDto loginWithAuthorizationCode(String token, KakaoToken kakaoToken){
        KakaoUser kakaoUser = getUserFromToken(token);
        OAuthResponseDto response = KakaoUser.toUserInfo(kakaoUser.getKakaoAccount(), kakaoToken);
        return response;
    }

    public LoginResponseDto login(String token){
        KakaoUser kakaoUser = getUserFromToken(token);
        LoginResponseDto response = KakaoUser.toUserInfo(kakaoUser.getKakaoAccount());
        Long userId = register(kakaoUser);
        response.setUserId(userId);

        return response;
    }

    public KakaoUser getUserFromToken(String token) {
        HttpURLConnection con = httpURLConnection(token, USER_INFO_URI);
        int responseCode = 0;
        KakaoUser kakaoUser = null;
        try {
            responseCode = con.getResponseCode();

            if (responseCode == HttpStatus.OK.value()) {
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                Gson gson = new Gson();
                kakaoUser = gson.fromJson(sb.toString(), KakaoUser.class);
            }
            else{
                throw new InvalidSocialTokenException();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return kakaoUser;
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
