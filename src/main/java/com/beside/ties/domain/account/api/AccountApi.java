package com.beside.ties.domain.account.api;

import com.beside.ties.domain.account.dto.request.*;
import com.beside.ties.domain.account.dto.response.AccountInfoResponse;
import com.beside.ties.domain.account.dto.response.ClassmateDetailResponse;
import com.beside.ties.domain.account.dto.response.ClassmateResponse;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


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

    @Operation(summary = "프로필 정보 수정")
    @PutMapping("/profile")
    public ResponseEntity<String> updateUserInfo(
            @CurrentUser Account account,
            @RequestBody AccountUpdateRequest request
    ){
        String message = accountService.updateUserInfo(request, account);
        return ResponseEntity.ok().body(message);
    }

    @Operation(summary = "이름 및 닉네임 수정")
    @PutMapping("/name")
    public ResponseEntity<String> updateNameAndNickname(
            @CurrentUser Account account,
            @RequestBody AccountUpdateNameRequest request
    ){
        String message = accountService.updateNameAndNickName(request, account);
        return ResponseEntity.ok().body(message);
    }

    @Operation(summary = "학교 정보 수정")
    @PutMapping("/school")
    public ResponseEntity<String> updateSchool(
            @CurrentUser Account account,
            @RequestBody AccountUpdateSchoolRequest request
    ){
        String message = accountService.updateSchool(account, request);
        return ResponseEntity.ok().body(message);
    }

    @Operation(summary = "동창 목록 조회")
    @GetMapping("/classmates")
    public List<ClassmateResponse> getSchoolmateList(
            @CurrentUser Account account
    ){
        return accountService.findClassMateList(account);
    }

    @Operation(summary = "동창 단건 조회")
    @GetMapping("/classmate/detail")
    public ResponseEntity<ClassmateDetailResponse> getSchoolmateDetail(
            @RequestParam("schoolmate_id") Long schoolmateId
    ){
        ClassmateDetailResponse response = accountService.findSchoolmateDetail(schoolmateId);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "이미지 수정")
    @PutMapping("/image")
    public ResponseEntity<String> updateImage(
            @CurrentUser Account account,
            @RequestPart("file") MultipartFile multipartFile
    ) throws IOException {
        String message =
                accountService.uploadImage(multipartFile, account);
        return ResponseEntity.ok().body(message);
    }

    @Operation(summary = "나의 정보 조회")
    @GetMapping("/myinfo")
    public ResponseEntity<AccountInfoResponse> getUserInfo(
            @CurrentUser Account account
    ){
        AccountInfoResponse response = accountService.findByAccount(account);
        return ResponseEntity.ok().body(response);
    }
}
