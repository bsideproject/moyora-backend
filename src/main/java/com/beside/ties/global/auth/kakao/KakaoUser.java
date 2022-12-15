package com.beside.ties.global.auth.kakao;

import com.beside.ties.domain.account.dto.response.LoginResponse;
import com.beside.ties.domain.account.dto.response.OAuthResponse;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class KakaoUser {

     public static OAuthResponse toUserInfo(KakaoAccount account, KakaoToken kakaoToken){
        return new OAuthResponse(
                account.getEmail(),
                account.getProfile().getProfileImageUrl(),
                account.getProfile().getNickname(),
                account.getName(),
                kakaoToken
        );

    }

    public static LoginResponse toUserInfo(KakaoAccount account){
        return new LoginResponse(
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