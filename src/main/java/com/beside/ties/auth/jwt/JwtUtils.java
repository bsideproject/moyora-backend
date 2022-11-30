package com.beside.ties.auth.jwt;

import com.beside.ties.domain.users.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import org.springframework.data.util.Pair;

import java.security.Key;
import java.util.Date;

public class JwtUtils {
    //exception 및 토큰 검증 로직 필요
    public static String getUsername(String token) {
        // jwtToken에서 username을 찾습니다.
        return Jwts.parserBuilder()
                .setSigningKeyResolver(SigningKeyResolver.instance)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // username
    }

    public static String createToken(Users user,JwtType jwtType) {
        Claims claims = Jwts.claims().setSubject(user.getUsername()); // subject
        Date now = new Date(); // 현재 시간
        Pair<String, Key> key = JwtKey.getRandomKey();
        long ext;

        if(jwtType == JwtType.ACCESS_TOKEN)
            ext = JwtProperties.ACCESS_EXPIRATION_TIME;
        else
            ext = JwtProperties.REFRESH_EXPIRATION_TIME;

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ext))
                .setHeaderParam(JwsHeader.KEY_ID, key.getFirst())
                .signWith(key.getSecond())
                .compact();
    }




}