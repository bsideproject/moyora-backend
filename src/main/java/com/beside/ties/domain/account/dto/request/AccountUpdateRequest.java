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
            value = "state",
            example = "도",
            required = true
    )
    String state;

    @ApiModelProperty(
            value = "region",
            example = "도시",
            required = false
    )
    String city;

    @ApiModelProperty(
            value = "job",
            example = "백엔드 개발자",
            required = true
    )
    String job;

    @ApiModelProperty(
            value = "birthday",
            example = "생일",
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
