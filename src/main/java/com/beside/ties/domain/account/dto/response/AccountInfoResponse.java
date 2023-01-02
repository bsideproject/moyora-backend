package com.beside.ties.domain.account.dto.response;

import com.beside.ties.domain.account.entity.MBTI;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
@ApiModel
public class AccountInfoResponse {

    @ApiModelProperty(
            value = "profile",
            example = "이미지url"
    )
    public String profile;

    @ApiModelProperty(
            value = "username",
            example = "별명"
    )
    public String username;

    @JsonProperty("school_name")
    @ApiModelProperty(
            value = "schoolName",
            example = "신월초등하교"
    )
    public String schoolName;

    @JsonProperty("phone")
    @ApiModelProperty(
            value = "휴대전화",
            example = "01012345678"
    )
    public String phone;

    @ApiModelProperty(
            value = "state",
            example = "도, 시"
    )
    public String state;

    @ApiModelProperty(
            value = "city",
            example = "도시"
    )
    public String city;

    @ApiModelProperty(
            value = "job",
            example = "백엔드 개발자",
            required = true
    )
    public String job;

    @ApiModelProperty(
            value = "nickname",
            example = "짠돌이",
            required = false
    )
    public String nickname;

    @ApiModelProperty(
            value = "mbti",
            example = "INFP",
            required = false
    )
    public MBTI mbti;

    @ApiModelProperty(
            value = "sns1",
            example = "youtubelink",
            required = false
    )
    public String sns1;

    @ApiModelProperty(
            value = "sns2",
            example = "facebook",
            required = false
    )
    public String sns2;

    @ApiModelProperty(
            value = "sns3",
            example = "instagram",
            required = false
    )
    public String sns3;

}
