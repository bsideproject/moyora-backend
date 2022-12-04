package com.beside.ties.dto.user.response;

import com.beside.ties.auth.kakao.KakaoToken;
import lombok.Data;

@Data
public class LoginResponseDto {

    private Long userId;
    public final String email;
    public final String profileImageUrl;
    public final String nickname;
    private final String name;
}
