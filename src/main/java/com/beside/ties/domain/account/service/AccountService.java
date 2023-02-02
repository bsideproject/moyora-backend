package com.beside.ties.domain.account.service;

import com.beside.ties.domain.account.dto.request.*;
import com.beside.ties.domain.account.dto.response.AccountInfoResponse;
import com.beside.ties.domain.account.dto.response.ClassmateDetailResponse;
import com.beside.ties.domain.account.dto.response.ClassmateResponse;
import com.beside.ties.domain.account.entity.QAccount;
import com.beside.ties.domain.jobcategory.entity.JobCategory;
import com.beside.ties.domain.jobcategory.repo.JobCategoryRepo;
import com.beside.ties.domain.jobcategory.service.JobCategoryService;
import com.beside.ties.domain.region.entity.Region;
import com.beside.ties.domain.region.repo.RegionRepo;
import com.beside.ties.domain.school.entity.School;
import com.beside.ties.domain.school.repo.SchoolRepo;
import com.beside.ties.domain.school.service.SchoolService;
import com.beside.ties.domain.notebox.entity.NoteBox;
import com.beside.ties.domain.notebox.repo.NoteBoxRepo;
import com.beside.ties.domain.schoolguestbook.entity.SchoolGuestBook;
import com.beside.ties.domain.schoolguestbook.repo.SchoolGuestBookRepo;
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

        Account account = accountRepo.findById(accountId).get();

        if(account.school != null){
            throw new IllegalArgumentException("이미 회원가입한 유저입니다.");
        }

        // 학교 존재 여부 확인
        Optional<School> optionalSchool = schoolService.checkSchoolCode(request.getSchoolCode());
        // 없으면 학교 등록
        if(optionalSchool.isEmpty()){
            throw new IllegalArgumentException("학교 정보가 잘못되었습니다. 학교를 다시 입력하여 주세요 ");
        }

        if(!request.getSchoolComment().isEmpty() && !request.getSchoolComment().isBlank()) {
            SchoolGuestBook schoolGuestBook = new SchoolGuestBook(optionalSchool.get(), account, request.getSchoolComment());
            SchoolGuestBook savedSchoolGuestBook = schoolGuestBookRepo.save(schoolGuestBook);
            logger.info("school guest book Id : " + savedSchoolGuestBook.getId());
        }


        Optional<NoteBox> optionalNoteBox = noteBoxRepo.findByAccount(account);
        if(optionalNoteBox.isPresent()){
            throw new IllegalArgumentException("이미 쪽지함이 생성되어 있습니다.");
        }
        // 유저 방명록 생성
        NoteBox noteBox1 = noteBoxRepo.save(new NoteBox(account));
        logger.info("유저 쪽지함이 생성이 ID"+ noteBox1.getId()+" 로 생성되었습니다.");

        // 직업 조회
        JobCategory jobCategory = jobCategoryService.findJobCategoryByName(request.getJob());

        // 지역 업데이트
        Optional<Region> regionOptional = regionRepo.findById(request.getRegionId());
        if(regionOptional.isEmpty()) throw new IllegalArgumentException("존재하지 않는 지역입니다.");

        // 회원 정보 업데이트
        account.secondaryInput(request, optionalSchool.get(), jobCategory,regionOptional.get());

        return "회원가입이 완료되었습니다.";
    }

    public String updateUserInfo(AccountUpdateRequest request, Long accountId){
        Account account = accountRepo.findById(accountId).get();

        Optional<Region> regionOptional = regionRepo.findById(request.getRegionId());
        if(regionOptional.isEmpty()) throw new IllegalArgumentException("존재하지 않는 지역입니다.");

        Optional<JobCategory> optionalJobCategory = jobCategoryRepo.findJobCategoryByName(request.getJob());
        if(optionalJobCategory.isEmpty()) throw new IllegalArgumentException("존재하지 않는 직업입니다.");

        account.updateProfile(request,optionalJobCategory.get(), regionOptional.get());

        return "유저 프로필 정보가 업데이트 되었습니다.";
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

        account.updateSchool(request, optionalSchool.get());

        return "학교 정보가 업데이트 되었습니다.";
    }

    public List<ClassmateResponse> findClassMateList(Account account, String name) {
        if(name == null) {
            return accountRepo.findAllBySchool(account.school).stream().map(ClassmateResponse::toDto).collect(Collectors.toList());
        }
        else{
            return accountRepo.findAllBySchoolAndNameStartsWith(account.school, name).stream().map(ClassmateResponse::toDto).collect(Collectors.toList());
        }
    }

    public ClassmateDetailResponse findSchoolmateDetail(Long schoolmateId) {
        Optional<Account> accountOptional = accountRepo.findById(schoolmateId);
        if(accountOptional.isEmpty()){
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }

        Account account = accountOptional.get();

        String graduate = getGraduate(account);

        if(account.isPublic){
            return ClassmateDetailResponse.builder()
                    .birthDate(account.getBirthDate().toString().replace('-','.').substring(2,10))
                    .facebook(account.getFacebook())
                    .instagram(account.getInstagram())
                    .youtube(account.getYoutube())
                    .mbti(account.getMbti())
                    .nickname(account.getNickname())
                    .username(account.getName())
                    .profile(account.getProfile())
                    .schoolName(account.getSchool().getSchoolName() + graduate)
                    .residence(account.getRegion().getParent().getName()+" "+parseRegion(account.getRegion().getName()))
                    .job(account.getMyJob().getName())
                    .jobCategory(account.getMyJob().getParent().getName())
                    .build();

        }else{
            return ClassmateDetailResponse.builder()
                    .birthDate(account.getBirthDate().toString().replace('-','.').substring(2,10))
                    .facebook(account.getFacebook())
                    .instagram(account.getInstagram())
                    .youtube(account.getYoutube())
                    .mbti(account.getMbti())
                    .profile(account.getProfile())
                    .nickname(account.getNickname())
                    .username(account.getName())
                    .schoolName(account.getSchool().getSchoolName() + graduate)
                    .residence(null)
                    .job(null)
                    .jobCategory(null)
                    .build();
        }

    }

    private String getGraduate(Account account) {
        int establishmentDate = Integer.parseInt(account.school.getEstablishmentDate().substring(0,4));
        int nowYear = Year.now().getValue();
        int graduateYear = nowYear - establishmentDate;
        String graduate = "(" + graduateYear + "회 졸업)";
        return graduate;
    }

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

    public long getAllSchoolMateCount(Long schoolId) {

        Optional<School> schoolOptional = schoolRepo.findById(schoolId);
        if(schoolOptional.isEmpty()){
            throw new IllegalArgumentException("존재하지 않는 학교를 조회했습니다.");
        }

        return accountRepo.countAllBySchool(schoolOptional.get());
    }


    public String deleteMe(Long accountId) {
        Account account = accountRepo.findById(accountId).get();
        account.deleteMe();
        return "삭제되었습니다.";

    }
}
