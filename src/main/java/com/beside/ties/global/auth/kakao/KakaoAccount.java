package com.beside.ties.global.auth.kakao;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class KakaoAccount {

    @SerializedName("age_range")
    private final String ageRange;

    @SerializedName("age_range_needs_agreement")
    private final Boolean ageRangeNeedsAgreement;

    @SerializedName("birthday")
    private final String birthday;

    @SerializedName("birthday_needs_agreement")
    private final Boolean birthdayNeedsAgreement;

    @SerializedName("birthday_type")
    private final String birthdayType;

    @SerializedName("birthyear")
    private final String birthyear;

    @SerializedName("birthyear_needs_agreement")
    private final Boolean birthyearNeedsAgreement;

    @SerializedName("email")
    private final String email;

    @SerializedName("email_needs_agreement")
    private final Boolean emailNeedsAgreement;

    @SerializedName("has_age_range")
    private final Boolean hasAgeRange;

    @SerializedName("has_birthday")
    private final Boolean hasBirthday;

    @SerializedName("has_birthyear")
    private final Boolean hasBirthyear;

    @SerializedName("has_email")
    private final Boolean hasEmail;

    @SerializedName("has_phone_number")
    private final Boolean hasPhoneNumber;

    @SerializedName("is_email_valid")
    private final Boolean isEmailValid;

    @SerializedName("is_email_verified")
    private final Boolean isEmailVerified;

    @SerializedName("name")
    private final String name;

    @SerializedName("name_needs_agreement")
    private final Boolean nameNeedsAgreement;

    @SerializedName("phone_number")
    private final String phoneNumber;

    @SerializedName("phone_number_needs_agreement")
    private final Boolean phoneNumberNeedsAgreement;

    @SerializedName("profile_nickname_needs_agreement")
    private final Boolean profileNicknameNeedsAgreement;

    @SerializedName("profile_image_needs_agreement")
    private final Boolean profileImageNeedsAgreement;

    @SerializedName("profile")
    private final KakaoProfile profile;

}