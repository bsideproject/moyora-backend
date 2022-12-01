package com.beside.ties.rest;


import com.beside.ties.auth.kakao.KakaoAPI;
import com.beside.ties.auth.kakao.KakaoToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class HomeController {

    @Autowired
    private final KakaoAPI kakaoAPI;

    @GetMapping
    public String home() {
        return "Hello, World!";
    }

    @PostMapping("/login")
    ResponseEntity<KakaoToken> kakaoLogin(
            @RequestParam("code") String code
    ){
        KakaoToken kakaoToken = kakaoAPI.getAccessToken(code);
        return ResponseEntity.ok().body(kakaoToken);
    }

    @PostMapping("/logout")
    ResponseEntity<String> kakaoLogout(
            @RequestParam("accessToken") String accessToken
    ){
        kakaoAPI.kakaoLogout(accessToken);
        return ResponseEntity.ok().body("logout success");
    }

}
