package com.beside.ties.domain.account.service;

import com.beside.ties.domain.account.dto.request.*;
import com.beside.ties.domain.account.dto.response.AccountInfoResponse;
import com.beside.ties.domain.account.dto.response.ClassmateDetailResponse;
import com.beside.ties.domain.account.dto.response.ClassmateResponse;
import com.beside.ties.domain.account.mapper.AccountMapper;
import com.beside.ties.domain.jobcategory.entity.JobCategory;
import com.beside.ties.domain.jobcategory.repo.JobCategoryRepo;
import com.beside.ties.domain.jobcategory.service.JobCategoryService;
import com.beside.ties.domain.region.entity.Region;
import com.beside.ties.domain.region.repo.RegionRepo;
import com.beside.ties.domain.school.entity.School;
import com.beside.ties.domain.school.repo.SchoolRepo;
import com.beside.ties.domain.school.service.SchoolService;
import com.beside.ties.domain.userguestbook.entity.UserGuestBook;
import com.beside.ties.global.auth.kakao.KakaoUser;
import com.beside.ties.global.auth.security.jwt.JwtDto;
import com.beside.ties.global.auth.security.jwt.JwtType;
import com.beside.ties.global.auth.security.jwt.JwtUtil;
import com.beside.ties.global.common.NaverUploader;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private final SchoolRepo schoolRepo;
    private final NaverUploader naverUploader;

    public String uploadImage(MultipartFile multipartFile, Account account) throws IOException {
        String substring = String.valueOf(UUID.randomUUID()).substring(1, 22);
        String uploadImage = naverUploader.upload(multipartFile, substring);
        Account account1 = accountRepo.findById(account.id).get();
        account1.updateImage(uploadImage);
        return "????????? ????????? ????????? ?????????????????????.";
    }

    public JwtDto kakaoSignIn(HttpServletRequest request){
        String token = RequestUtil.getAuthorizationToken(request.getHeader("Authorization"));
        Account account = null;
        KakaoUser kakaoUser = getUserFromToken(token);
        boolean isFirst = false;
        Optional<Account> optionalAccount = accountRepo.findAccountByKakaoId(kakaoUser.getId());
        // kakakoUser ????????? ????????? ????????? ????????? ????????? ?????? ????????? ?????? ?????? ?????? ??????????????? ????????????.
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

        if(save == null) throw new IllegalArgumentException("?????? ?????? ??????");
        logger.debug("?????? ?????? ??????");

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
        logger.debug("id "+save.getId()+"??? ????????? ?????????????????????.");

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
            throw new IllegalArgumentException("?????? ????????? ???????????? ????????????.");
        }
        return accountByEmail.get();
    }

    public String secondarySignUp(AccountSecondarySignUpRequest request, Account account) {

        // ?????? ?????? ?????? ??????
        Optional<School> optionalSchool = schoolService.checkSchoolCode(request.getSchoolCode());
        // ????????? ?????? ??????
        if(optionalSchool.isEmpty()){
            throw new IllegalArgumentException("?????????????????? ?????? ???????????????. ????????? ?????? ??????????????????.");
        }

        // ?????? ????????? ??????
        UserGuestBook userGuestBook = new UserGuestBook(account);
        logger.info("?????? ????????? ????????? ID"+userGuestBook.getId()+" ??? ?????????????????????.");

        // ?????? ??????
        JobCategory jobCategory = jobCategoryService.findJobCategoryByName(request.getJob());

        // ?????? ????????????
        Optional<Region> regionOptional = regionRepo.findRegionByName(request.getCity());
        if(regionOptional.isEmpty()) throw new IllegalArgumentException("???????????? ?????? ???????????????.");

        // ?????? ?????? ????????????
        account.secondaryInput(request, optionalSchool.get(), jobCategory,regionOptional.get());

        return "??????????????? ?????????????????????.";
    }

    public String updateUserInfo(AccountUpdateRequest request, Account account){
        Account account1 = accountRepo.findAccountByKakaoId(account.getKakaoId()).get();

        Optional<Region> regionOptional = regionRepo.findRegionByName(request.getCity());
        if(regionOptional.isEmpty()) throw new IllegalArgumentException("???????????? ?????? ???????????????.");

        Optional<JobCategory> optionalJobCategory = jobCategoryRepo.findJobCategoryByName(request.getJob());
        if(optionalJobCategory.isEmpty()) throw new IllegalArgumentException("???????????? ?????? ???????????????.");

        account1.updateProfile(request,optionalJobCategory.get(), regionOptional.get());

        return "?????? ????????? ????????? ???????????? ???????????????.";
    }

    public AccountInfoResponse findByAccount(Account account) {
        return accountMapper.toAccountInfoResponse(account);
    }

    public String updateNameAndNickName(AccountUpdateNameRequest request, Account account) {
        Account account1 = accountRepo.findById(account.id).get();
        account1.updateNameAndNickName(request);
        return "?????? ??? ???????????? ???????????? ???????????????.";
    }

    public String updateSchool(Account account, AccountUpdateSchoolRequest request) {

        Account account1 = accountRepo.findById(account.id).get();
        Optional<School> optionalSchool = schoolRepo.findSchoolBySchoolCode(request.getSchoolCode());
        if(optionalSchool.isEmpty()){
            throw new IllegalArgumentException("???????????? ?????? ???????????????.");
        }

        account1.updateSchool(request, optionalSchool.get());

        return "?????? ????????? ???????????? ???????????????.";
    }

    public List<ClassmateResponse> findClassMateList(Account account) {
        return accountRepo.findAllBySchool(account.school).stream().map(ClassmateResponse::toDto).collect(Collectors.toList());
    }

    public ClassmateDetailResponse findSchoolmateDetail(Long schoolmateId) {
        Optional<Account> accountOptional = accountRepo.findById(schoolmateId);
        if(accountOptional.isEmpty()){
            throw new IllegalArgumentException("???????????? ?????? ???????????????.");
        }

        Account account = accountOptional.get();

        if(account.privateSetting){
            return ClassmateDetailResponse.builder()
                    .birthDate(account.getBirthDate())
                    .facebook(account.getFacebook())
                    .instagram(account.getInstagram())
                    .youtube(account.getYoutube())
                    .mbti(account.getMbti())
                    .nickname(account.getNickname())
                    .username(account.getUsername())
                    .schoolName(account.getSchool().getSchoolName())
                    .city(null)
                    .state(null)
                    .job(null)
                    .jobCategory(null)
                    .build();

        }else{
            return ClassmateDetailResponse.builder()
                    .birthDate(account.getBirthDate())
                    .facebook(account.getFacebook())
                    .instagram(account.getInstagram())
                    .youtube(account.getYoutube())
                    .mbti(account.getMbti())
                    .nickname(account.getNickname())
                    .username(account.getUsername())
                    .schoolName(account.getSchool().getSchoolName())
                    .city(account.getRegion().getName())
                    .state(account.getRegion().getParent().getName())
                    .job(account.getMyJob().getName())
                    .jobCategory(account.getMyJob().getParent().getName())
                    .build();
        }

    }
}
