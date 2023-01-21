package com.beside.ties.global.auth.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.beside.ties.global.common.exception.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final static Logger log = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.secret}")
    private String secret;


    public String createJwt(String kakaoId, JwtType jwtType) {

        log.info(jwtType+" 토큰 생성, kakaoId: " + kakaoId);
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());

        Long tokenExpiredTime;

        if(jwtType == JwtType.ACCESS_TOKEN){
            tokenExpiredTime = JwtExpirationTime.getTime(24*7);
        }else{
            tokenExpiredTime = JwtExpirationTime.getTime(24*30);
        }

        String token = JWT.create()
                .withSubject(jwtType.name())
                .withIssuer(issuer)
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenExpiredTime))
                .withClaim("kakaoId", kakaoId)
                .sign(algorithm);

        return token;
    }

    public JwtType getSubject(String token) {
        String result = validationToken(token).getSubject();
        log.info("토큰 " + token);
        log.info("토큰 subject [sub: "+result+"] ]");

        if(result.equals(JwtType.ACCESS_TOKEN.name())){
               return JwtType.ACCESS_TOKEN;
        }else{
            return JwtType.REFRESH_TOKEN;
        }
    }

    public String getClaim(String token, String claim) {
        String result = validationToken(token)
                .getClaim(claim)
                .asString();

        log.info("토큰 클레임 ["+claim+" : "+result+"]");
        return result;
    }

    private DecodedJWT validationToken(String token){

        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());

        try{
            return JWT
                    .require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token);
        } catch (AlgorithmMismatchException e) {
            log.error("잘못된 서명입니다. [token: $token]");
            throw new AlgorithmMismatchException("잘못된 서명입니다.");
        } catch (JWTDecodeException e) {
            log.error("토큰 형식이 잘못되었습니다. [token: $token]");
            throw new JWTDecodeException("토큰 형식이 잘못되었습니다.");
        } catch (TokenExpiredException e) {
            log.error("토큰 유효기간 만료 [token: $token]");
            throw new TokenExpiredException("토큰 유효가간이 만료되었습니다.");
        }
    }

}
