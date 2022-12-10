package com.beside.ties.dto.account.response;

import com.beside.ties.auth.kakao.KakaoToken;
import lombok.Data;

@Data
public class OAuthResponseDto {

    public final String email;
    public final String profileImageUrl;
    public final String nickname;
    private final String name;

    public final KakaoToken kakaoToken;
}
