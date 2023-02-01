package com.beside.ties.domain.account.dto.response;

import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.account.entity.MBTI;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import static com.beside.ties.global.common.RequestUtil.parseRegion;

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
            value = "거주 지역",
            example = "서울특별시 강남구"
    )
    public String residence;

    @ApiModelProperty(
            value = "job_category",
            example = "IT 직군",
            required = true
    )
    public String jobCategory;

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
    public String birthDate;

    @ApiModelProperty(
            value = "공개 여부",
            example = "true",
            required = false
    )
    public Boolean isPublic;

    public static AccountInfoResponse toDto(Account account, String graduate){

        if(account.getBirthDate() == null){
            return AccountInfoResponse.builder()
                    .name(account.getName())
                    .birthDate(null)
                    .residence(account.getRegion().getParent().getName() +" "+parseRegion(account.getRegion().getName()))
                    .facebook(account.getFacebook())
                    .instagram(account.getInstagram())
                    .youtube(account.getYoutube())
                    .mbti(account.getMbti())
                    .jobCategory(account.getMyJob().getParent().getName())
                    .schoolId(account.getSchool().getId())
                    .nickname(account.getNickname())
                    .job(account.getMyJob().getName())
                    .schoolName(account.getSchool().getSchoolName()+graduate)
                    .isPublic(account.getIsPublic())
                    .profile(account.getProfile())
                    .build();
        }else {
            return AccountInfoResponse.builder()
                    .name(account.getName())
                    .birthDate(account.getBirthDate().toString().replace('-', '.').substring(2, 10))
                    .residence(account.getRegion().getParent().getName() + " " + parseRegion(account.getRegion().getName()))
                    .facebook(account.getFacebook())
                    .instagram(account.getInstagram())
                    .youtube(account.getYoutube())
                    .jobCategory(account.getMyJob().getParent().getName())
                    .mbti(account.getMbti())
                    .schoolId(account.getSchool().getId())
                    .nickname(account.getNickname())
                    .job(account.getMyJob().getName())
                    .schoolName(account.getSchool().getSchoolName() + graduate)
                    .isPublic(account.getIsPublic())
                    .profile(account.getProfile())
                    .build();
        }
    }

}
