package com.beside.ties.auth.kakao;

public class KakaoToken {

    public String accessToken;
    public String refreshToken;

    KakaoToken(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
