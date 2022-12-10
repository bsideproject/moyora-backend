package com.beside.ties.dto.account.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@NoArgsConstructor
@Getter
@ApiModel
public class KakaoTokenRequest{

    @NotBlank
    @ApiModelProperty(
            value = "카카오 Access Token",
            required = true
    )
    private String token;
}