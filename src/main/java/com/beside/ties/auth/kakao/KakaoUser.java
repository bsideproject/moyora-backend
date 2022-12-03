package com.beside.ties.auth.kakao;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.val;

@Data
public class KakaoUser {

    @SerializedName("connected_at")
    private final String connectedAt;

    @SerializedName("id")
    private final String id;

    @SerializedName("kakao_account")
    private final KakaoAccount kakaoAccount;

    @SerializedName("synched_at")
    private final String synchedAt;
}