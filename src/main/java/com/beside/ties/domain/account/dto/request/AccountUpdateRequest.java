package com.beside.ties.domain.account.dto.request;

import com.beside.ties.domain.account.entity.MBTI;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@ApiModel
public class AccountUpdateRequest {

    @ApiModelProperty(
            value = "지역 ID",
            example = "10201",
            required = true
    )
    Long regionId;

    @ApiModelProperty(
            value = "job",
            example = "백엔드 개발자",
            required = true
    )
    String job;


    @ApiModelProperty(
            value = "필수정보 비공개 세팅",
            example = "true",
            required = true
    )
    Boolean isPublic;


    @ApiModelProperty(
            value = "birthday",
            example = "1996-01-01",
            required = true
    )
    String birthdate;

    @ApiModelProperty(
            value = "mbti",
            example = "INFP",
            required = false
    )
    MBTI mbti;
    @ApiModelProperty(
            value = "facebook",
            example = "facebook_link",
            required = false
    )
    String facebook;
    @ApiModelProperty(
            value = "youtube",
            example = "youtube_link",
            required = false
    )
    String youtube;
    @ApiModelProperty(
            value = "instagram",
            example = "instagram_link",
            required = false
    )
    String instagram;
}
