package com.beside.ties.service;

import com.beside.ties.auth.kakao.KakaoToken;
import com.beside.ties.auth.kakao.KakaoUser;
import com.beside.ties.common.exception.custom.InvalidSocialTokenException;
import com.beside.ties.domain.account.Account;
import com.beside.ties.domain.account.AccountRepo;
import com.beside.ties.domain.account.mapper.AccountMapper;
import com.beside.ties.dto.account.response.LoginResponseDto;
import com.beside.ties.dto.account.response.OAuthResponseDto;
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
import java.util.Optional;

import static com.beside.ties.auth.kakao.KakaoOAuthConstants.USER_INFO_URI;

@RequiredArgsConstructor
@Transactional
@Service
public class AccountService {

    Logger logger = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepo accountRepo;
    private final AccountMapper accountMapper;

    private final PasswordEncoder passwordEncoder;

    public Long register(KakaoUser kakaoUser){
        Account account = Account.toUserFromKakao(kakaoUser);
        account.UpdatePassword(passwordEncoder.encode(account.getPw()));
        Account save = accountRepo.save(account);

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

        Optional<Account> optionalAccount = accountRepo.findAccountByKakaoId(kakaoUser.getId());
        if(optionalAccount.isPresent()){
            Account account1 = optionalAccount.get();
            response = accountMapper.toLoginResponseDto(account1);
        }
        else{
            Long userId = register(kakaoUser);
            response.userId = userId;
            response.isFirst = true;
        }

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

    public Account loadUserByUsername(String email) {
        Optional<Account> accountByEmail = accountRepo.findAccountByEmail(email);
        if(accountByEmail.isEmpty()){
            throw new IllegalArgumentException("유저 정보가 존재하지 않습니다.");
        }
        return accountByEmail.get();
    }
}
