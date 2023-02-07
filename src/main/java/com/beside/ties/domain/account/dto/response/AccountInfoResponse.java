package com.beside.ties.domain.account.dto.response;

import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.account.entity.MBTI;
import com.beside.ties.domain.account.service.AccountService;
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
            value = "계정 ID",
            example = "10020"
    )
    public Long id;

    @ApiModelProperty(
            value = "지역 ID",
            example = "150"
    )
    public Long regionId;

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
            value = "schoolCode",
            example = "321334",
            required = false
    )
    public String schoolCode;

    @ApiModelProperty(
            value = "공개 여부",
            example = "true",
            required = false
    )
    public Boolean isPublic;

    @ApiModelProperty(
            value = "졸업 년도",
            example = "2002",
            required = true
    )
    public int graduationYear;




    public static AccountInfoResponse toDto(Account account, String graduate){

            return AccountInfoResponse.builder()
                    .name(account.getName())
                    .birthDate(getBirthDate(account))
                    .residence(getResidence(account))
                    .facebook(account.getFacebook())
                    .schoolCode(getSchoolCode(account))
                    .regionId(getRegionId(account))
                    .instagram(account.getInstagram())
                    .id(account.getId())
                    .youtube(account.getYoutube())
                    .jobCategory(getParentJob(account))
                    .mbti(getMbti(account))
                    .schoolId(getSchoolId(account))
                    .nickname(account.getNickname())
                    .job(getJob(account))
                    .graduationYear(account.getGraduationYear())
                    .schoolName(getSchoolNameWithGraduate(account,graduate))
                    .isPublic(account.getIsPublic())
                    .profile(account.getProfile())
                    .build();
    }

    public static MBTI getMbti(Account account){
        if(account.getMbti() == null) return null;
        return MBTI.valueOf(account.getMbti().getName());
    }


    static String getSchoolCode(Account account){
        if(account.getSchool() == null) return "";
        if(account.getSchool().getSchoolCode() == null) return "";
        return account.school.getSchoolCode();
    }

    static Long getRegionId(Account account){
        if(account.getRegion() == null) return 111L;
        return account.getRegion().getId();
    }

    static String getBirthDate(Account account){
        if(account.getBirthDate() == null) return "";
        return AccountService.getBirthDate(account);
    }

    static String getResidence(Account account){
        if(account.getRegion() == null) return "";
        return account.getRegion().getParent().getName() + " " + parseRegion(account.getRegion().getName());
    }

    static String getParentJob(Account account){
        if(account.getMyJob() == null) return "";
        return account.getMyJob().getParent().getName();
    }

    static String getJob(Account account){
        if(account.getMyJob() == null) return "";
        return account.getMyJob().getName();
    }

    static String getSchoolNameWithGraduate(Account account, String graduate){
        if(account.getSchool() == null) return "";
        return  account.getSchool().getSchoolName() + graduate;
    }

    static Long getSchoolId(Account account){
        if(account.getSchool() == null) return 2L;
        return  account.getSchool().getId();
    }


}
