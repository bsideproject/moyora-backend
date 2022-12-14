package com.beside.ties.domain.account.dto.response;

import com.beside.ties.domain.account.entity.MBTI;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("school_name")
    @ApiModelProperty(
            value = "schoolName",
            example = "신월초등하교"
    )
    public String schoolName;


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

    @JsonProperty("job_category")
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
    public LocalDate birthDate;


}