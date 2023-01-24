package com.beside.ties.domain.account.dto.response;

import com.beside.ties.domain.account.entity.MBTI;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Getter
public class ClassmateDetailResponse {

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

    @ApiModelProperty(
            value = "nickname",
            example = "짠돌이",
            required = false
    )
    public String nickname;

    @ApiModelProperty(
            value = "schoolName",
            example = "신월초등하교"
    )
    public String schoolName;


    @ApiModelProperty(
            value = "거주 지역",
            example = "서울특별시 강남구"
    )
    public String residence;

    @ApiModelProperty(
            value = "job",
            example = "백엔드 개발자",
            required = true
    )
    public String job;

    @ApiModelProperty(
            value = "job_category",
            example = "IT 직군",
            required = true
    )
    public String jobCategory;

    @ApiModelProperty(
            value = "mbti",
            example = "INFP",
            required = false
    )
    public MBTI mbti;

    @ApiModelProperty(
            value = "instagram",
            example = "instagram",
            required = false
    )
    public String instagram;

    @ApiModelProperty(
            value = "youtube",
            example = "youtube",
            required = false
    )
    public String youtube;

    @ApiModelProperty(
            value = "facebook",
            example = "facebook",
            required = false
    )
    public String facebook;

    @ApiModelProperty(
            value = "birthDate",
            example = "1996.07.25",
            required = false
    )
    public String birthDate;


}