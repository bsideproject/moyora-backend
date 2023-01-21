package com.beside.ties.domain.account.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@ApiModel
public class LoginResponse {

    @ApiModelProperty(
            value = "유저 아이디",
            required = true,
            example = "10000"
    )
    public Long userId;

    @ApiModelProperty(
            value = "이메일",
            required = true,
            example = "moyora@moyora.com"
    )
    public String email;

    @ApiModelProperty(
            value = "프로파일 이미지",
            required = true,
            example = "fuifjewoifewiuewyfu.com"
    )
    public String profileImageUrl;

    @ApiModelProperty(
            value = "유저 닉네임",
            required = true,
            example = "godric"
    )
    public String nickname;

    @ApiModelProperty(
            value = "유저 이름",
            required = true,
            example = "10000"
    )
    public String name;

    @ApiModelProperty(
            value = "오늘 회원가입 하셨나요?",
            required = true,
            example = "true"
    )
    public boolean isFirst;

    public LoginResponse(String email, String profileImageUrl, String nickname, String username, boolean isFirst) {
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.nickname = nickname;
        this.name = username;
        this.isFirst = isFirst;
    }
}
