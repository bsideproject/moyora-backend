package com.beside.ties.domain.account.service;

import com.beside.ties.domain.account.dto.request.AccountUpdateRequest;
import com.beside.ties.domain.account.dto.request.LocalSignUpRequest;
import com.beside.ties.domain.jobcategory.entity.JobCategory;
import com.beside.ties.domain.jobcategory.service.JobCategoryService;
import com.beside.ties.domain.school.entity.School;
import com.beside.ties.domain.school.service.SchoolService;
import com.beside.ties.global.auth.kakao.KakaoToken;
import com.beside.ties.global.auth.kakao.KakaoUser;
import com.beside.ties.global.common.exception.custom.InvalidSocialTokenException;
import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.account.repo.AccountRepo;
import com.beside.ties.domain.account.mapper.AccountMapper;
import com.beside.ties.domain.account.dto.response.LoginResponse;
import com.beside.ties.domain.account.dto.response.OAuthResponse;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

import static com.beside.ties.global.auth.kakao.KakaoOAuthConstants.USER_INFO_URI;

@RequiredArgsConstructor
@Transactional
@Service
public class AccountService {

    Logger logger = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepo accountRepo;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;
    private final SchoolService schoolService;
    private final JobCategoryService jobCategoryService;

    public Long register(KakaoUser kakaoUser){
        Account account = Account.toUserFromKakao(kakaoUser);
        account.UpdatePassword(passwordEncoder.encode(account.getPw()));
        Account save = accountRepo.save(account);

        if(save == null) throw new IllegalArgumentException("유저 저장 실패");
        logger.debug("유저 저장 성공");

        return save.getId();

    }

    public Long localSignUp(LocalSignUpRequest request){
        Account account = Account.builder()
                .email(request.getEmail())
                .phoneNum(request.getPhoneNum())
                .profile(request.getProfile())
                .username(request.getUsername())
                .kakaoId("sfoiusdjfioswf")
                .nickname(request.getNickname()).build();

        Account save = accountRepo.save(account);
        logger.debug("id "+save.getId()+"로 계정이 저장되었습니다.");

        return save.id;
    }

    public OAuthResponse loginWithAuthorizationCode(String token, KakaoToken kakaoToken){
        KakaoUser kakaoUser = getUserFromToken(token);
        OAuthResponse response = KakaoUser.toUserInfo(kakaoUser.getKakaoAccount(), kakaoToken);
        return response;
    }

    public LoginResponse login(String token){
        KakaoUser kakaoUser = getUserFromToken(token);
        LoginResponse response = KakaoUser.toUserInfo(kakaoUser.getKakaoAccount());

        Optional<Account> optionalAccount = accountRepo.findAccountByKakaoId(kakaoUser.getId());
        if(optionalAccount.isPresent()){
            Account account1 = optionalAccount.get();
            response = accountMapper.toLoginResponseDto(account1);
        }
        else{
            Long userId = register(kakaoUser);
            response.userId = userId;
            response.isFirst = true;
        }

        return response;
    }

    public KakaoUser getUserFromToken(String token) {
        HttpURLConnection con = httpURLConnection(token, USER_INFO_URI);
        int responseCode = 0;
        KakaoUser kakaoUser = null;
        try {
            responseCode = con.getResponseCode();

            if (responseCode == HttpStatus.OK.value()) {
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                Gson gson = new Gson();
                kakaoUser = gson.fromJson(sb.toString(), KakaoUser.class);
            }
            else{
                throw new InvalidSocialTokenException();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return kakaoUser;
    }

    private HttpURLConnection httpURLConnection(String AccessToken, String requestUrl) {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Authorization", "Bearer " + AccessToken);
            return con;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Account loadUserByUsername(String email) {
        Optional<Account> accountByEmail = accountRepo.findAccountByEmail(email);
        if(accountByEmail.isEmpty()){
            throw new IllegalArgumentException("유저 정보가 존재하지 않습니다.");
        }
        return accountByEmail.get();
    }

    public void secondarySignUp(AccountUpdateRequest request, Account account) {

        // 학교 존재 여부 확인
        Optional<School> optionalSchool = schoolService.checkSchoolCode(request.getSchoolCode());
        // 없으면 학교 등록
        if(optionalSchool.isEmpty()){
            School school = School.builder()
                    .address(request.getAddress())
                    .establishmentDate(request.getEstablishmentDate())
                    .schoolCode(request.getSchoolCode())
                    .schoolName(request.getSchoolName())
                    .build();

            Long schoolId = schoolService.save(school);
            logger.info("id" + schoolId + " school 등록 완료");
        }

        // 직업 조회
        JobCategory jobCategory = jobCategoryService.findJobCategoryByName(request.getJob());

        // 지역 업데이트
        account.updateRegion(request.getState(), request.getCity());

        // 회원 정보 업데이트
        account.secondaryInput(request, optionalSchool.get(), jobCategory);
    }
}
