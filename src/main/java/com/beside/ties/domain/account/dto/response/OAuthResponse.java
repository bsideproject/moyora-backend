package com.beside.ties.domain.account.dto.response;

import com.beside.ties.global.auth.kakao.KakaoToken;
import lombok.Data;

@Data
public class OAuthResponse {

    public final String email;
    public final String profileImageUrl;
    public final String nickname;
    private final String name;

    public final KakaoToken kakaoToken;
}
