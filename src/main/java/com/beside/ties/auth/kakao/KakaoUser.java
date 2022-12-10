package com.beside.ties.auth.kakao;

import com.beside.ties.dto.account.response.LoginResponseDto;
import com.beside.ties.dto.account.response.OAuthResponseDto;
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
                null,
                null,
                account.getName(),
                false
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