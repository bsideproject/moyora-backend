package com.beside.ties.domain.users;

import com.beside.ties.auth.kakao.KakaoAccount;
import com.beside.ties.auth.kakao.KakaoUser;
import com.beside.ties.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "USERS_SEQ_GEN",
        sequenceName = "USERS_SEQ",
        initialValue = 10000,
        allocationSize = 50
)
@Getter
@Entity
public class Users extends BaseTimeEntity implements UserDetails {

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
        this.role = Role.USER;
        this.nickname = nickname;
        this.phoneNum = phoneNum;
        this.phoneKey = phoneKey;
        this.username = username;
        this.profile = profile;
    }

    @Id
    @GeneratedValue(generator = "USERS_SEQ_GEN")
    @Column(name = "users_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(length = 25)
    private String password;

    @Column(length = 50)
    private String nickname;

    @Column(name = "phone_num",length = 11)
    private String phoneNum;

    @Column(nullable = false, unique = true,name = "phone_key")
    private String phoneKey;

    @Column(length = 25)
    private String username;

    @Column(nullable = false)
    private String profile;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public static Users toUserFromKakao(
            KakaoUser kakaoUser
    ){
        KakaoAccount account = kakaoUser.getKakaoAccount();
        String phone = null;
        String profile = "default";
        String nickname = null;
        String email = String.valueOf(UUID.randomUUID()).substring(1,22);

        if(account.getPhoneNumberNeedsAgreement() != null)
            if(account.getPhoneNumberNeedsAgreement())
                phone = account.getPhoneNumber();
        if(account.getProfileNicknameNeedsAgreement() != null)
            if(account.getProfileNicknameNeedsAgreement())
                nickname = account.getProfile().getNickname();
        if(account.getProfileImageNeedsAgreement())
            if(account.getProfileImageNeedsAgreement())
                profile = account.getProfile().getProfileImageUrl();
        if(account.getEmail() != null)
            email = account.getEmail();

        return Users.builder()
                .email(email)
                .nickname(nickname)
                .phoneKey(kakaoUser.getId())
                .phoneNum(phone)
                .username(null)
                .profile(profile)
                .build();
    }

    @Override
    public String getPassword() {
        return getPassword();
    }

    @Override
    public String getUsername() {
        return getPhoneKey();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+getRole().getName()));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
