package com.beside.ties.domain.account.api;

import com.beside.ties.domain.account.dto.request.AccountSecondarySignUpRequest;
import com.beside.ties.domain.account.dto.request.AccountUpdateRequest;
import com.beside.ties.domain.account.dto.request.LocalSignUpRequest;
import com.beside.ties.domain.account.dto.response.AccountInfoResponse;
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

    @Operation(summary = "로컬 테스트용 회원가입")
    @PostMapping("/local/signup")
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


    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<String> updateProfile(
            @RequestBody AccountSecondarySignUpRequest request,
            @CurrentUser Account account
    ){
        String message = accountService.secondarySignUp(request, account);
        return ResponseEntity.ok().body(message);
    }

    @Operation(summary = "유저 정보 수정")
    @PutMapping
    public ResponseEntity<String> updateUserInfo(
            @CurrentUser Account account,
            @RequestBody AccountUpdateRequest request
    ){
        String message = accountService.updateUserInfo(request, account);
        return ResponseEntity.ok().body(message);
    }

    @Operation(summary = "유저 정보 조회")
    @GetMapping
    public ResponseEntity<AccountInfoResponse> getUserInfo(
            @CurrentUser Account account
    ){
        AccountInfoResponse response = accountService.findByAccount(account);
        return ResponseEntity.ok().body(response);
    }
}
