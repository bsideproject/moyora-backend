package com.beside.ties.global.auth.kakao;

import lombok.Getter;
import lombok.Setter;

@Getter
public class KakaoToken {

    private String accessToken;
    private String refreshToken;

    KakaoToken(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
