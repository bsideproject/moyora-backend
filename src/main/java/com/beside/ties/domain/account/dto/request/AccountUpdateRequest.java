package com.beside.ties.domain.account.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ApiModel
public class AccountUpdateRequest {

        @ApiModelProperty(
                value = "user_id",
                example = "10003",
                required = true
        )
        Long userId;

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
        @JsonProperty("graduation_year")
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
        @JsonProperty("parent_region")
        String State;

        @ApiModelProperty(
            value = "child_region",
            example = "성산구",
            required = true
        )
        @JsonProperty("child_region")
        String city;

        @ApiModelProperty(
            value = "school_name",
            example = "떙떙초등학교",
            required = true
        )
        String schoolName;

        @ApiModelProperty(
                value = "establishment_date",
                example = "2022.10.12",
                required = true
        )
        @JsonProperty("establishment_date")
        String establishmentDate;

        @ApiModelProperty(
            value = "address",
            example = "학교 주소",
            required = true
        )
        String address;
        @ApiModelProperty(
            value = "school_code",
            example = "학교 코드xaaa34",
            required = true
        )
        @JsonProperty("school_code")
        String schoolCode;

}
