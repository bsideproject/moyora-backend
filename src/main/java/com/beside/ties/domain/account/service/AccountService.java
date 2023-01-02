package com.beside.ties.domain.account.service;

import com.beside.ties.domain.account.dto.request.AccountSecondarySignUpRequest;
import com.beside.ties.domain.account.dto.request.AccountUpdateRequest;
import com.beside.ties.domain.account.dto.request.LocalSignUpRequest;
import com.beside.ties.domain.account.dto.response.AccountInfoResponse;
import com.beside.ties.domain.account.mapper.AccountMapper;
import com.beside.ties.domain.jobcategory.entity.JobCategory;
import com.beside.ties.domain.jobcategory.repo.JobCategoryRepo;
import com.beside.ties.domain.jobcategory.service.JobCategoryService;
import com.beside.ties.domain.region.entity.Region;
import com.beside.ties.domain.region.repo.RegionRepo;
import com.beside.ties.domain.school.entity.School;
import com.beside.ties.domain.school.service.SchoolService;
import com.beside.ties.domain.userguestbook.entity.UserGuestBook;
import com.beside.ties.global.auth.kakao.KakaoUser;
import com.beside.ties.global.auth.security.jwt.JwtDto;
import com.beside.ties.global.auth.security.jwt.JwtType;
import com.beside.ties.global.auth.security.jwt.JwtUtil;
import com.beside.ties.global.common.RequestUtil;
import com.beside.ties.global.common.exception.custom.InvalidSocialTokenException;
import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.account.repo.AccountRepo;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final SchoolService schoolService;
    private final JobCategoryService jobCategoryService;
    private final RegionRepo regionRepo;
    private final JobCategoryRepo jobCategoryRepo;

    private final AccountMapper accountMapper;

    public JwtDto kakaoSignIn(HttpServletRequest request){
        String token = RequestUtil.getAuthorizationToken(request.getHeader("Authorization"));
        Account account = null;
        KakaoUser kakaoUser = getUserFromToken(token);
        boolean isFirst = false;
        Optional<Account> optionalAccount = accountRepo.findAccountByKakaoId(kakaoUser.getId());
        // kakakoUser 정보를 통해서 유저가 등록된 유저일 경우 토큰을 반환 아닐 경우 회원가입을 진행한다.
        if (optionalAccount.isEmpty()) {
            isFirst = true;
            register(kakaoUser);
        }
        account = accountRepo.findAccountByKakaoId(kakaoUser.getId()).get();

        String accessToken = jwtUtil.createJwt(account.getUsername(), JwtType.ACCESS_TOKEN);
        String refreshToken = jwtUtil.createJwt(account.getUsername(), JwtType.REFRESH_TOKEN);
        return new JwtDto(accessToken, refreshToken, isFirst);

    }

    public Long register(KakaoUser kakaoUser){
        Account account = Account.toUserFromKakao(kakaoUser);
        account.updatePassword(passwordEncoder.encode(account.getPw()));
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

    public String secondarySignUp(AccountSecondarySignUpRequest request, Account account) {

        // 학교 존재 여부 확인
        Optional<School> optionalSchool = schoolService.checkSchoolCode(request.getSchoolCode());
        // 없으면 학교 등록
        if(optionalSchool.isEmpty()){
            throw new IllegalArgumentException("등록되어있지 않은 학교입니다. 관리자 문의 부탁드립니다.");
        }

        // 유저 방명록 생성
        UserGuestBook userGuestBook = new UserGuestBook(account);
        logger.info("유저 방명록 생성이 ID"+userGuestBook.getId()+" 로 생성되었습니다.");

        // 직업 조회
        JobCategory jobCategory = jobCategoryService.findJobCategoryByName(request.getJob());

        // 지역 업데이트
        Optional<Region> regionOptional = regionRepo.findRegionByName(request.getCity());
        if(regionOptional.isEmpty()) throw new IllegalArgumentException("존재하지 않는 지역입니다.");

        // 회원 정보 업데이트
        account.secondaryInput(request, optionalSchool.get(), jobCategory,regionOptional.get());

        return "회원가입이 완료되었습니다.";
    }

    public String updateUserInfo(AccountUpdateRequest request, Account account){
        Optional<Account> accountOptional = accountRepo.findAccountByKakaoId(account.getKakaoId());
        if(accountOptional.isEmpty()) throw new IllegalArgumentException("등록되지 않은 유저입니다.");
        Account account1 = accountOptional.get();

        Optional<Region> regionOptional = regionRepo.findRegionByName(request.getCity());
        if(regionOptional.isEmpty()) throw new IllegalArgumentException("존재하지 않는 지역입니다.");

        Optional<JobCategory> optionalJobCategory = jobCategoryRepo.findJobCategoryByName(request.getJob());
        if(optionalJobCategory.isEmpty()) throw new IllegalArgumentException("존재하지 않는 직업입니다.");

        account1.updateAll(request,optionalJobCategory.get(), regionOptional.get());

        return "유저 정보가 업데이트 되었습니다.";
    }

    public AccountInfoResponse findByAccount(Account account) {
        return accountMapper.toAccountInfoResponse(account);
    }
}
