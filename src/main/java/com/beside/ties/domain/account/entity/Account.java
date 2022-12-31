package com.beside.ties.domain.account.entity;

import com.beside.ties.domain.account.dto.request.AccountUpdateRequest;
import com.beside.ties.domain.school.entity.School;
import com.beside.ties.global.auth.kakao.KakaoAccount;
import com.beside.ties.global.auth.kakao.KakaoUser;
import com.beside.ties.global.common.BaseTimeEntity;
import com.beside.ties.domain.account.Role;
import com.beside.ties.domain.jobcategory.entity.JobCategory;
import com.beside.ties.domain.userguestbook.entity.UserGuestBook;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "ACCOUNT_SEQ_GEN",
        sequenceName = "ACCOUNT_SEQ",
        initialValue = 10000,
        allocationSize = 50
)
@Getter
@Entity
public class Account extends BaseTimeEntity implements UserDetails {

    @Builder
    public Account(
            String email,
            String profile,
            String nickname,
            String username,
            String phoneNum,
            String kakaoId
    )
    {
        this.email = email;
        this.pw = "123";
        this.role = Role.USER;
        this.nickname = nickname;
        this.phoneNum = phoneNum;
        this.kakaoId = kakaoId;
        this.username = username;
        this.profile = profile;
    }


    @Id
    @GeneratedValue(generator = "ACCOUNT_SEQ_GEN")
    @Column(name = "account_id")
    public Long id;

    @Column(nullable = false, unique = true, length = 50)
    public String email;

    @Column(name = "graduation_year")
    int graduationYear;

    @Column(length = 75)
    private String pw;

    @Column(length = 50)
    public String nickname;

    @Column(name = "phone_num",length = 11)
    private String phoneNum;

    @Column(nullable = false, unique = true,name = "kakao_id")
    private String kakaoId;

    @Column(length = 25)
    public String username;

    @Column(nullable = false)
    public String profile;

    @Column(length = 40)
    String state;

    @Column(length = 40)
    String city;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "account", cascade = CascadeType.ALL)
    UserGuestBook userGuestBooks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    School school;


    @Column(name = "sns_account", length = 50)
    String snsAccount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_category_id")
    JobCategory myJob;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;



    public static Account toUserFromKakao(
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

        if(account.getEmail() != null)
            email = account.getEmail();

        return Account.builder()
                .email(email)
                .nickname(nickname)
                .kakaoId(kakaoUser.getId())
                .phoneNum(phone)
                .username(null)
                .profile(profile)
                .build();
    }

    public void secondaryInput(AccountUpdateRequest request, School school, JobCategory job){
        this.myJob = job;
        this.school = school;
        this.username = request.getName();
        this.nickname = request.getNickname();
        this.graduationYear = request.getGraduationYear();
    }

    public void updatePassword(String password){
        this.pw = password;
    }

    public void updateRegion(String state, String city){
        this.state = state;
        this.city = city;
    }

    @Override
    public String getPassword() {
        return getPw();
    }

    @Override
    public String getUsername() {
        return getKakaoId();
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
