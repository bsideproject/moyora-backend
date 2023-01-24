package com.beside.ties.domain.account.dto.response;

import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.account.entity.MBTI;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
@ApiModel
public class AccountInfoResponse {

    @ApiModelProperty(
            value = "profile",
            example = "이미지url"
    )
    public String profile;

    @ApiModelProperty(
            value = "이름",
            example = "김찬수"
    )
    public String name;

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
            value = "schoolID",
            example = "학교ID"
    )
    public Long schoolId;

    @ApiModelProperty(
            value = "휴대전화",
            example = "01012345678"
    )
    public String phoneNum;

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

    @ApiModelProperty(
            value = "private_setting",
            example = "true",
            required = false
    )
    public Boolean privateSetting;

    public static AccountInfoResponse toDto(Account account){
        return AccountInfoResponse.builder()
                .name(account.getName())
                .birthDate(account.getBirthDate())
                .city(account.getRegion().getName())
                .state(account.getRegion().getParent().getName())
                .facebook(account.getFacebook())
                .instagram(account.getInstagram())
                .youtube(account.getYoutube())
                .mbti(account.getMbti())
                .schoolId(account.getSchool().getId())
                .nickname(account.getNickname())
                .job(account.getMyJob().getName())
                .schoolName(account.getSchool().getSchoolName())
                .privateSetting(account.getPrivateSetting())
                .profile(account.getProfile())
                .phoneNum(account.getPhoneNum())
                .build();
    }

}
