package com.beside.ties.domain.account.api;

import com.beside.ties.domain.account.dto.request.AccountUpdateRequest;
import com.beside.ties.domain.account.dto.request.LocalSignUpRequest;
import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.account.service.AccountService;
import com.beside.ties.global.auth.security.jwt.JwtDto;
import com.beside.ties.global.common.annotation.CurrentUser;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Api(tags = "유저 API")
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@RestController
public class AccountApi {

    private final AccountService accountService;

    @Operation(summary = "로컬 테스트용 회원가입 1단계")
    @PostMapping("/local/login")
    public ResponseEntity<String> localSignUp(
            @RequestBody LocalSignUpRequest request
            ){
        Long id = accountService.localSignUp(request);
        return ResponseEntity.status(HttpStatus.OK).body("id "+id+"로 계정이 저장되었습니다.");
    }
    @Operation(summary = "카카오 로그인")
    @PostMapping("/signin")
    public ResponseEntity<JwtDto> kakaoSignIn(HttpServletRequest request){
        JwtDto jwtDto = accountService.kakaoSignIn(request);
        return ResponseEntity.ok().body(jwtDto);
    }


    @Operation(summary = "회원가입 2단계 프로필 정보 등록")
    @PostMapping("/secondarysignup")
    public void updateProfile(
            @RequestBody AccountUpdateRequest request,
            @CurrentUser Account account
    ){
        accountService.secondarySignUp(request, account);
    }
}
