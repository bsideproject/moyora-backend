package com.beside.ties.domain.account.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LocalSignUpRequest {
    String email;
    String profile;
    String nickname;
    String username;
    String phoneNum;
}
