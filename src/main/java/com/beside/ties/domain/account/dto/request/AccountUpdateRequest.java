package com.beside.ties.domain.account.dto.request;

import com.beside.ties.domain.account.entity.MBTI;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
@ApiModel
public class AccountUpdateRequest {

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
            value = "nickname",
            example = "짠돌이",
            required = false
    )
    String nickname;

    @ApiModelProperty(
            value = "mbti",
            example = "INFP",
            required = false
    )
    MBTI mbti;
    @ApiModelProperty(
            value = "sns1",
            example = "youtubelink",
            required = false
    )
    String sns1;
    @ApiModelProperty(
            value = "sns2",
            example = "facebook",
            required = false
    )
    String sns2;
    @ApiModelProperty(
            value = "sns3",
            example = "instagram",
            required = false
    )
    String sns3;
}
