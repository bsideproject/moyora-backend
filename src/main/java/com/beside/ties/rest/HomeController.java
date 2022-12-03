package com.beside.ties.rest;


import com.beside.ties.auth.kakao.KakaoAPI;
import com.beside.ties.auth.kakao.KakaoToken;
import com.beside.ties.dto.user.request.KakaoTokenRequest;
import com.beside.ties.dto.user.response.LoginResponseDto;
import com.beside.ties.dto.user.response.OAuthResponseDto;
import com.beside.ties.service.UsersService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Api(tags = "기본 Api")
@RequiredArgsConstructor
@RestController
public class HomeController {

    @Autowired
    private final KakaoAPI kakaoAPI;

    @Autowired
    private final UsersService usersService;

    @GetMapping
    public String home() {
        return "Hello, World!";
    }

    @Operation(summary = "인가 코드를 받을 경우의 로그인")
    @PostMapping("/oauth/token")
    ResponseEntity<OAuthResponseDto> getToken(
            @RequestParam("code") String code
    ){
        KakaoToken kakaoToken = kakaoAPI.getAccessToken(code);
        String token = kakaoToken.getAccessToken();

        OAuthResponseDto response = usersService.login(token, kakaoToken);

        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "카카오 엑세스 토큰을 바로 전달 받을 경우의 로그인")
    @PostMapping("/login")
    ResponseEntity<LoginResponseDto> kakaoLogin(
            @Validated @RequestBody KakaoTokenRequest request){
        LoginResponseDto response = usersService.login(request.getToken());

        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "카카오 로그아웃")
    @PostMapping("/logout")
    ResponseEntity<String> kakaoLogout(
            @Validated @RequestBody KakaoTokenRequest request
    ){
        kakaoAPI.kakaoLogout(request.getToken());
        return ResponseEntity.ok().body("logout success");
    }

}
