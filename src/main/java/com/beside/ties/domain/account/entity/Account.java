package com.beside.ties.domain.account.entity;

import com.beside.ties.domain.account.dto.request.AccountUpdateNameRequest;
import com.beside.ties.domain.account.dto.request.AccountSecondarySignUpRequest;
import com.beside.ties.domain.account.dto.request.AccountUpdateRequest;
import com.beside.ties.domain.account.dto.request.AccountUpdateSchoolRequest;
import com.beside.ties.domain.region.entity.Region;
import com.beside.ties.domain.school.entity.School;
import com.beside.ties.global.auth.kakao.KakaoAccount;
import com.beside.ties.global.auth.kakao.KakaoUser;
import com.beside.ties.global.common.BaseTimeEntity;
import com.beside.ties.domain.account.Role;
import com.beside.ties.domain.jobcategory.entity.JobCategory;
import com.beside.ties.domain.notebox.entity.NoteBox;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.beside.ties.global.common.NaverUploader.nCloudUrl;

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
            String name,
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
        this.name = name;
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
    public String phoneNum;

    @Column(length = 11)
    public LocalDate birthDate;

    @Column(nullable = false, unique = true,name = "kakao_id")
    private String kakaoId;

    @Column(length = 60)
    public String name;

    @Column(nullable = false)
    public String profile;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    public MBTI mbti = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    public Region region;


    @OneToOne(fetch = FetchType.LAZY, mappedBy = "account", cascade = CascadeType.ALL)
    NoteBox noteBox;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    public School school;


    @Column(name = "instagram", length = 50)
    public String instagram;

    @Column(name = "facebook", length = 50)
    public String facebook;

    @Column(name = "youtube", length = 50)
    public String youtube;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_category_id")
    public JobCategory myJob;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "is_public", nullable = false)
    public Boolean isPublic = true;



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
                .name(null)
                .profile(profile)
                .build();
    }

    public void secondaryInput(AccountSecondarySignUpRequest request, School school, JobCategory job, Region region){
        this.myJob = job;
        this.school = school;
        this.name = request.getName();
        this.nickname = request.getNickname();
        this.graduationYear = request.getGraduationYear();
        this.region = region;
    }

    public void updateProfile(AccountUpdateRequest request, JobCategory job, Region region){
        this.mbti = request.getMbti();
        this.instagram = request.getInstagram();
        this.youtube = request.getYoutube();
        this.facebook = request.getFacebook();
        this.myJob = job;
        this.region = region;
        this.birthDate = LocalDate.parse(request.getBirthdate(), DateTimeFormatter.ISO_DATE);
        this.isPublic = request.getIsPublic();
    }

    public void updateImage(String image){
        this.profile = nCloudUrl+image;
    }

    public void updateNameAndNickName(AccountUpdateNameRequest request){
        this.nickname = request.getNickname();
        this.name = request.getName();
    }

    public void updateSchool(AccountUpdateSchoolRequest request, School school){
        this.graduationYear = request.getGraduationYear();
        this.school = school;
    }

    public void updatePassword(String password){
        this.pw = password;
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
