package com.beside.ties.rest.account;

import com.beside.ties.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "유저 API")
@RequiredArgsConstructor
@RestController("/api/v1/user")
public class AccountController {

    private final AccountService accountService;


    @Operation(summary = "프로필 정보 수정")
    @PostMapping("/profile")
    public void updateProfile(){

    }
}
