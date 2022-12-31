package com.beside.ties.global.auth.security.jwt;

import org.springframework.stereotype.Component;


class JwtExpirationTime {

    public static Long getTime(int hours) {
        return 1000L * 60 * 60 * hours;
    }
}