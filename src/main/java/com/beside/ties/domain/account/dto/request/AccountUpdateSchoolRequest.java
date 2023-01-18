package com.beside.ties.domain.account.dto.request;

import lombok.Getter;

@Getter
public class AccountUpdateSchoolRequest {

    private String schoolCode;

    private int graduationYear;
}
