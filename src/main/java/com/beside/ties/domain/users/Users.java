package com.beside.ties.domain.users;

import com.beside.ties.auth.kakao.KakaoAccount;
import com.beside.ties.auth.kakao.KakaoUser;
import com.beside.ties.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "USERS_SEQ_GEN",
        sequenceName = "USERS_SEQ",
        initialValue = 10000,
        allocationSize = 50
)
@Getter
@Entity
public class Users extends BaseTimeEntity {

    @Builder
    public Users(
            String email,
            String profile,
            String nickname,
            String username,
            String phoneNum,
            String phoneKey
    )
    {
        this.email = email;
        this.password = null;
        this.nickname = nickname;
        this.phoneNum = phoneNum;
        this.phoneKey = phoneKey;
        this.username = username;
        this.profile = profile;
    }

    @Id
    @GeneratedValue(generator = "USERS_SEQ_GEN")
    @Column(name = "users_id")
    Long id;

    @Column(nullable = false, unique = true, length = 50)
    String email;

    @Column(length = 25)
    String password;

    @Column(length = 50)
    String nickname;

    @Column(name = "phone_num",length = 11)
    String phoneNum;

    @Column(nullable = false, unique = true,name = "phone_key")
    String phoneKey;

    @Column(nullable = false, length = 25)
    String username;

    @Column(nullable = false)
    String profile;

    public static Users toUserFromKakao(
            KakaoUser kakaoUser
    ){
        KakaoAccount account = kakaoUser.getKakaoAccount();
        String phone = null;
        String profile = "default";
        String nickname = null;

        if(account.getPhoneNumberNeedsAgreement())
            phone = account.getPhoneNumber();
        if(account.getProfileNicknameNeedsAgreement())
            nickname = account.getNickname();
        if(account.getProfileImageNeedsAgreement())
            profile = account.getProfileImageUrl();

        return Users.builder()
                .email(account.getEmail())
                .nickname(nickname)
                .phoneKey(kakaoUser.getId())
                .phoneNum(phone)
                .username(account.getName())
                .profile(profile)
                .build();
    }

}
