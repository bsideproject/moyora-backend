package com.beside.ties.auth.kakao;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class KakaoProfile {

    @SerializedName("nickname")
    private final String nickname;

    @SerializedName("profile_image_url")
    private final String profileImageUrl;

}
