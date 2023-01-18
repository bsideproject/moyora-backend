package com.beside.ties.global.auth.security.jwt;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@ApiModel
public class JwtDto {

    @ApiModelProperty(
            value = "엑세스 토큰",
            required = true
    )
    public String accessToken;
    @ApiModelProperty(
            value = "리프레쉬토큰",
            required = true
    )
    public String refreshToken;
    @ApiModelProperty(
            value = "회원가입한 유저인가?",
            required = true
    )
    public Boolean isFirst;
}
