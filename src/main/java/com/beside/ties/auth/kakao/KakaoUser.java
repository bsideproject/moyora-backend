package com.beside.ties.auth.kakao;

import com.beside.ties.dto.user.response.LoginResponseDto;
import com.beside.ties.dto.user.response.OAuthResponseDto;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class KakaoUser {

     public static OAuthResponseDto toUserInfo(KakaoAccount account, KakaoToken kakaoToken){
        return new OAuthResponseDto(
                account.getEmail(),
                account.getProfile().getProfileImageUrl(),
                account.getProfile().getNickname(),
                account.getName(),
                kakaoToken
        );

    }

    public static LoginResponseDto toUserInfo(KakaoAccount account){
        return new LoginResponseDto(
                account.getEmail(),
                account.getProfile().getProfileImageUrl(),
                account.getProfile().getNickname(),
                account.getName()
        );

    }

    @SerializedName("connected_at")
    private final String connectedAt;

    @SerializedName("id")
    private final String id;

    @SerializedName("kakao_account")
    private final KakaoAccount kakaoAccount;

    @SerializedName("synched_at")
    private final String synchedAt;
}