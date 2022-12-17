package com.beside.ties.domain.account.api;

import com.beside.ties.domain.account.dto.request.AccountUpdateRequest;
import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.account.service.AccountService;
import com.beside.ties.global.common.annotation.CurrentUser;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "유저 API")
@RequiredArgsConstructor
@RestController("/api/v1/user")
public class AccountController {

    private final AccountService accountService;


    @Operation(summary = "회원가입 2단계 프로필 정보 등록")
    @PostMapping("/profile")
    public void updateProfile(
            @RequestBody AccountUpdateRequest request,
            @CurrentUser Account account
    ){
        accountService.updateAccount(request, account);
    }
}
