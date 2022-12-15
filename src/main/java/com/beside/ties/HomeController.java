package com.beside.ties;


import com.beside.ties.global.auth.kakao.KakaoAPI;
import com.beside.ties.global.common.annotation.CurrentUser;
import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.account.dto.request.KakaoTokenRequest;
import com.beside.ties.domain.account.dto.response.LoginResponse;
import com.beside.ties.domain.account.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Api(tags = "기본 Api")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class HomeController {

    private final KakaoAPI kakaoAPI;
    private final AccountService accountService;

    @GetMapping
    public String home() {
        return "Hello, World!";
    }


    @Operation(summary = "유저 권한 테스트")
    @PostMapping("/test")
    ResponseEntity<String> testAuth(
            @CurrentUser Account user
    ){
        return ResponseEntity.ok().body("유저 권한 테스트 성공");
    }

    @Operation(summary = "카카오 로그인")
    @PostMapping("/login")
    ResponseEntity<LoginResponse> kakaoLogin(
            @Validated @RequestBody KakaoTokenRequest request){
        LoginResponse response = accountService.login(request.getToken());

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
