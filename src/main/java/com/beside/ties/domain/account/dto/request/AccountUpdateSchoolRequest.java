package com.beside.ties.domain.account.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AccountUpdateSchoolRequest {

    @JsonProperty("school_code")
    private String schoolCode;

    @JsonProperty("graduation_year")
    private int graduationYear;
}
