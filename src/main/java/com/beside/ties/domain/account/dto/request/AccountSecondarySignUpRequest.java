package com.beside.ties.domain.account.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
@ApiModel
public class AccountSecondarySignUpRequest {

        @ApiModelProperty(
            value = "nickname",
            example = "짠돌이",
            required = false
        )
        String nickname;

        @ApiModelProperty(
            value = "name",
            example = "김철수",
            required = true
        )
        String name;

        @ApiModelProperty(
            value = "graduation_year",
            example = "2002",
            required = true
        )
        int graduationYear;

        @ApiModelProperty(
            value = "job",
            example = "백엔드 개발자",
            required = true
        )
        String job;

        @ApiModelProperty(
            value = "parent_region",
            example = "창원시",
            required = true
        )
        String State;

        @ApiModelProperty(
            value = "child_region",
            example = "성산구",
            required = true
        )
        String city;

        @ApiModelProperty(
            value = "school_code",
            example = "학교 코드xaaa34",
            required = true
        )
        String schoolCode;

}
