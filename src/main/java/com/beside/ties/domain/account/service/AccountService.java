package com.beside.ties.domain.account.service;

import com.beside.ties.domain.account.dto.request.*;
import com.beside.ties.domain.account.dto.response.AccountInfoResponse;
import com.beside.ties.domain.account.dto.response.ClassmateDetailResponse;
import com.beside.ties.domain.account.dto.response.ClassmateResponse;
import com.beside.ties.domain.account.entity.MBTI;
import com.beside.ties.domain.account.entity.QAccount;
import com.beside.ties.domain.common.dto.StatisticsRequestDto;
import com.beside.ties.domain.jobcategory.entity.JobCategory;
import com.beside.ties.domain.jobcategory.repo.JobCategoryRepo;
import com.beside.ties.domain.jobcategory.service.JobCategoryService;
import com.beside.ties.domain.mbti.entity.Mbti;
import com.beside.ties.domain.mbti.repo.MbtiRepo;
import com.beside.ties.domain.region.entity.Region;
import com.beside.ties.domain.region.repo.RegionRepo;
import com.beside.ties.domain.school.entity.School;
import com.beside.ties.domain.school.repo.SchoolRepo;
import com.beside.ties.domain.school.service.SchoolService;
import com.beside.ties.domain.notebox.entity.NoteBox;
import com.beside.ties.domain.notebox.repo.NoteBoxRepo;
import com.beside.ties.domain.schoolguestbook.entity.SchoolGuestBook;
import com.beside.ties.domain.schoolguestbook.repo.SchoolGuestBookRepo;
import com.beside.ties.domain.schooljob.service.SchoolJobService;
import com.beside.ties.domain.schoolmbti.service.SchoolMbtiService;
import com.beside.ties.domain.schoolregion.service.SchoolRegionService;
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
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.beside.ties.domain.account.dto.response.AccountInfoResponse.getMbti;
import static com.beside.ties.global.auth.kakao.KakaoOAuthConstants.USER_INFO_URI;
import static com.beside.ties.global.common.RequestUtil.parseRegion;

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
    private final SchoolRepo schoolRepo;
    private final NaverUploader naverUploader;
    private final NoteBoxRepo noteBoxRepo;
    private final SchoolGuestBookRepo schoolGuestBookRepo;
    private final EntityManager em;
    private final SchoolRegionService schoolRegionService;
    private final SchoolJobService schoolJobService;
    private final SchoolMbtiService schoolMbtiService;
    private final MbtiRepo mbtiRepo;


    public String uploadImage(MultipartFile multipartFile, Account account) throws IOException {
        String substring = String.valueOf(UUID.randomUUID()).substring(1, 22);
        String uploadImage = naverUploader.upload(multipartFile, substring);
        Account account1 = accountRepo.findById(account.id).get();
        account1.updateImage(uploadImage);
        return "프로필 이미지 변경이 완료되었습니다.";
    }

    public JwtDto kakaoSignIn(HttpServletRequest request){
        String token = RequestUtil.getAuthorizationToken(request.getHeader("Authorization"));
        Account account;
        KakaoUser kakaoUser = getUserFromToken(token);
        boolean isFirst = false;
        Optional<Account> optionalAccount = accountRepo.findAccountByKakaoId(kakaoUser.getId());
        // kakakoUser 정보를 통해서 유저가 등록된 유저일 경우 토큰을 반환 아닐 경우 회원가입을 진행한다.
        if (optionalAccount.isEmpty()) {
            isFirst = true;
            register(kakaoUser);
        }
        if(optionalAccount.isPresent()){
            School school = optionalAccount.get().getSchool();
            if(school == null){
                isFirst = true;
            }
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

        logger.debug("유저 저장 성공");

        return save.getId();

    }

    public Long localSignUp(LocalSignUpRequest request){
        Account account = Account.builder()
                .email(request.getEmail())
                .phoneNum(request.getPhoneNum())
                .profile(request.getProfile())
                .name(request.getUsername())
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


    public String secondarySignUp(AccountSecondarySignUpRequest request, Long accountId) {

        Account account = accountRepo.findById(accountId).orElseThrow();

        if(account.school != null){
            throw new IllegalArgumentException("이미 회원가입한 유저입니다.");
        }

        School school = schoolRepo.findSchoolBySchoolCode(request.getSchoolCode())
                .orElseThrow(() -> new IllegalArgumentException("학교 정보를 다시 선택해주세요!!"));


        if(!request.getSchoolComment().isEmpty() && !request.getSchoolComment().isBlank()) {
            SchoolGuestBook schoolGuestBook = new SchoolGuestBook(school, account, request.getSchoolComment());
            schoolGuestBook.settingRandomSticker();
            SchoolGuestBook savedSchoolGuestBook = schoolGuestBookRepo.save(schoolGuestBook);
            logger.info("school guest book Id : " + savedSchoolGuestBook.getId());
        }



        Optional<NoteBox> optionalNoteBox = noteBoxRepo.findByAccount(account);
        if(optionalNoteBox.isEmpty()){
            NoteBox noteBox1 = noteBoxRepo.save(new NoteBox(account));
            logger.info("유저 쪽지함이 생성이 ID"+ noteBox1.getId()+" 로 생성되었습니다.");
        }


        JobCategory jobCategory = jobCategoryService.findJobCategoryByName(request.getJob());
        schoolJobService.countPlus(account, jobCategory, school, Long.valueOf(request.getGraduationYear()));


        Optional<Region> regionOptional = regionRepo.findById(request.getRegionId());
        if(regionOptional.isEmpty()) throw new IllegalArgumentException("존재하지 않는 지역입니다.");
        schoolRegionService.countPlus(account, regionOptional.get(), school, Long.valueOf(request.getGraduationYear()));


        account.secondaryInput(request, school, jobCategory,regionOptional.get());


        return "회원가입이 완료되었습니다.";
    }

    public String updateUserInfo(AccountUpdateRequest request, Long accountId){
        Account account = accountRepo.findById(accountId).get();

        countMBTI(request, account);

        countJob(request, account);

        countRegion(request, account);


        Optional<Region> regionOptional = regionRepo.findById(request.getRegionId());
        if(regionOptional.isEmpty()) throw new IllegalArgumentException("존재하지 않는 지역입니다.");

        Optional<JobCategory> optionalJobCategory = jobCategoryRepo.findJobCategoryByName(request.getJob());
        if(optionalJobCategory.isEmpty()) throw new IllegalArgumentException("존재하지 않는 직업입니다.");

        Mbti mbti = null;
        if(request.getMbti() != null) {
            mbti = mbtiRepo.findByName(request.getMbti()).orElseThrow();
        }

        account.updateProfile(request,optionalJobCategory.get(), regionOptional.get(), mbti);

        return "유저 프로필 정보가 업데이트 되었습니다.";
    }

    private void countMBTI(AccountUpdateRequest request, Account account) {
        if(account.mbti == null && request.getMbti() != null){
            schoolMbtiService.countPlus(account, request.getMbti());
        }
        else if(account.mbti != null && request.getMbti() != null){

            if(!account.mbti.equals(request.getMbti())){
                schoolMbtiService.countPlus(account, request.getMbti());
                schoolMbtiService.countMinus(account, account.getMbti().getName());
            }

        }
    }

    private void countRegion(AccountUpdateRequest request, Account account){
        Optional<Region> regionOptional = regionRepo.findById(request.getRegionId());
        if(regionOptional.isPresent()) {
            if (account.region == null) {
                schoolRegionService.countPlus(account, regionOptional.get());
            }
            if(account.region.getId() != regionOptional.get().getId()){
                schoolRegionService.countMinus(account, account.getRegion());
                schoolRegionService.countPlus(account, regionOptional.get());
            }
        }
    }

    private void countJob(AccountUpdateRequest request, Account account) {
        JobCategory jobCategory = jobCategoryRepo.findJobCategoryByName(request.getJob()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직업입니다."));

        if(account.getMyJob() == null && request.getMbti() != null){
            schoolJobService.countPlus(account, jobCategory);
        }
        else if(account.getMyJob() != null && request.getJob() != null){

            if(!account.getMyJob().getParent().getName().equals(request.getJob())){
                schoolJobService.countPlus(account, jobCategory);
                schoolJobService.countMinus(account, account.getMyJob());
            }

        }

    }


    public AccountInfoResponse findByAccount(Long accountId) {

        Optional<Account> optional = accountRepo.findById(accountId);
        Account account = optional.get();
        String graduate = getGraduate(account);

        return AccountInfoResponse.toDto(account, graduate);
    }

    public String updateNameAndNickName(AccountUpdateNameRequest request, Long accountId) {
        Account account = accountRepo.findById(accountId).get();
        account.updateNameAndNickName(request);
        return "이름 및 닉네임이 업데이트 되었습니다.";
    }

    public String updateSchool(Long accountId, AccountUpdateSchoolRequest request) {

        Account account = accountRepo.findById(accountId).get();
        Optional<School> optionalSchool = schoolRepo.findSchoolBySchoolCode(request.getSchoolCode());
        if(optionalSchool.isEmpty()){
            throw new IllegalArgumentException("존재하지 않는 학교입니다.");
        }

        schoolRegionService.countMinus(account, account.getRegion());
        schoolJobService.countMinus(account, account.getMyJob());
        schoolMbtiService.countMinus(account, account.getMbti().getName());

        account.updateSchool(request, optionalSchool.get());

        schoolRegionService.countPlus(account, account.getRegion());
        schoolJobService.countPlus(account, account.getMyJob());
        schoolMbtiService.countPlus(account, account.getMbti().getName());

        return "학교 정보가 업데이트 되었습니다.";
    }

    public List<ClassmateResponse> findClassMateList(Account account, String name) {

        if(name == null) {
            return accountRepo.findAllBySchoolAndKakaoIdNotAndGraduationYear(account.school, account.getKakaoId(),account.getGraduationYear()).stream().map(ClassmateResponse::toDto).collect(Collectors.toList());
        }
        else{
            return accountRepo.findAllBySchoolAndNameContainsAndGraduationYear(account.school, name,account.getGraduationYear()).stream().map(ClassmateResponse::toDto).collect(Collectors.toList());
        }
    }

    public ClassmateDetailResponse findSchoolmateDetail(Long schoolmateId) {
        Optional<Account> accountOptional = accountRepo.findById(schoolmateId);
        if(accountOptional.isEmpty()){
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }

        Account account = accountOptional.get();

        String graduate = getGraduate(account);


        return ClassmateDetailResponse.builder()
                .birthDate(getBirthDate(account))
                .facebook(account.getFacebook())
                .instagram(account.getInstagram())
                .id(account.getId())
                .youtube(account.getYoutube())
                .mbti(getMbti(account))
                .profile(account.getProfile())
                .nickname(account.getNickname())
                .username(account.getName())
                .schoolName(getSchoolName(account,graduate))
                .residence(getResidence(account))
                .job(getJob(account))
                .jobCategory(getJobCategory(account))
                .build();

    }

    static String getSchoolName(Account account, String graduate){
        if(account.getSchool() == null) return "";
        return account.getSchool().getSchoolName() + graduate;
    }

    static String getResidence(Account account){
        if(account.getRegion() == null || account.isPublic == false) return "";
        return account.getRegion().getParent().getName()+" "+parseRegion(account.getRegion().getName());
    }

    static String getJob(Account account) {
        if(account.getMyJob() == null || account.isPublic == false) return "";
        return account.getMyJob().getName();
    }
    static String getJobCategory(Account account) {
        if(account.getMyJob() == null) return "";
        if(account.getMyJob().getParent() == null || account.isPublic == false) return "";
        return account.getMyJob().getParent().getName();
    }

    public static String getBirthDate(Account account){
        if(account.getBirthDate() == null){
            return "";
        }
        return account.getBirthDate().toString().replace('-','.').substring(0,10);
    }

    private String getGraduate(Account account) {
        if(account.school == null) return "";
        if(account.school.getEstablishmentDate() == null) return "";

        var myGraduate = account.getGraduationYear();
        if(account.getGraduationYear() == 0){
            return "";
        }else{
            myGraduate = account.getGraduationYear();
        }
        var establishmentDate = Integer.parseInt(account.school.getEstablishmentDate().substring(0,4));

        int graduateYear = myGraduate - establishmentDate;
        String graduate = "(" + graduateYear + "회 졸업)";
        return graduate;
    }
    //설립일 - 나의 졸업일

    public int getActivatedSchool(){
        JPAQueryFactory query = new JPAQueryFactory(em);
        QAccount qAccount = QAccount.account;
        return query.select(
                qAccount.school.id
        ).distinct().from(qAccount).fetch().size();
    }

    public void deleteAllInBatch() {
        accountRepo.deleteAllInBatch();
    }

    public long getAllUserCount() {

        return accountRepo.count();
    }

    public long getAllSchoolMateCount(Account account) {
        if(account.getSchool() == null) return 0L;
        return accountRepo.countAllBySchoolAndGraduationYear(account.getSchool(), account.getGraduationYear());
    }


    public String deleteMe(Long accountId) {
        Account account = accountRepo.findById(accountId).get();
        account.deleteMe();
        return "삭제되었습니다.";

    }
}
