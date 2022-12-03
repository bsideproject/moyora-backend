package com.beside.ties.auth.kakao;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class KakaoOAuthConstants {

    /** Encoded URL of Kakao's end-user authorization server. */
    public static final String AUTHORIZATION_SERVER_URL = "https://kauth.kakao.com/oauth/authorize";

    /** Encoded URL of Kakao's token server. */
    public static final String TOKEN_SERVER_URL = "https://kauth.kakao.com/oauth/token";

    /** Encoded URL of Kakao's user-info server. */
    public static final String USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";

}