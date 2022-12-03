package com.beside.ties.dto.user.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@ApiModel
public class KakaoTokenRequest{

    @NotBlank
    @ApiModelProperty(
            value = "카카오 Access Token",
            required = true
    )
    private final String token;
}