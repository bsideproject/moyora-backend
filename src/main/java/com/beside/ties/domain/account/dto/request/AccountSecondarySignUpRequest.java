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
            value = "지역 ID",
            example = "102",
            required = true
        )
        Long regionId;

        @ApiModelProperty(
            value = "school_code",
            example = "학교 코드xaaa34",
            required = true
        )
        String schoolCode;

        @ApiModelProperty(
                value = "School Guest Book Comment",
                example = "안녕 예들아 다시 만나서 반가워~",
                required = true
        )
        String schoolComment;

}
