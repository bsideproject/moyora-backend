package com.beside.ties.domain.account;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    GUEST("MANAGER", "매니저"),
    USER("USER", "유저"),
    ADMIN("ADMIN", "관리자");

    private final String name;
    private final String description;
}
