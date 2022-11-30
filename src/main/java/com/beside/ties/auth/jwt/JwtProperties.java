package com.beside.ties.auth.jwt;

/**
 * JWT 기본 설정값
 */
public class JwtProperties {
    private static final long SECOND = 1000;
    public static final long ACCESS_EXPIRATION_TIME = 3600*SECOND; // 1시간
    public static final long REFRESH_EXPIRATION_TIME = 24*60*60*SECOND; // 하루
    public static final String COOKIE_NAME = "JWT-AUTHENTICATION";
}
