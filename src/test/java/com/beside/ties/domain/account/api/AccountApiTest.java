package com.beside.ties.domain.account.api;

import com.beside.ties.domain.BaseMvcTest;
import com.beside.ties.domain.account.dto.request.AccountSecondarySignUpRequest;
import com.beside.ties.domain.account.dto.request.LocalSignUpRequest;
import com.beside.ties.domain.account.service.AccountService;
import com.beside.ties.domain.jobcategory.entity.JobCategory;
import com.beside.ties.domain.jobcategory.repo.JobCategoryRepo;
import com.beside.ties.domain.jobcategory.service.JobCategoryService;
import com.beside.ties.domain.region.repo.RegionRepo;
import com.beside.ties.domain.region.service.RegionService;
import com.beside.ties.domain.school.entity.School;
import com.beside.ties.domain.school.repo.SchoolRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.Filter;
import java.nio.charset.StandardCharsets;


@DisplayName("계정 API 테스트")
@Transactional
class AccountApiTest extends BaseMvcTest {

    private static final String phoneNum = "10188888888";
    private static final String email = "GODRI321C@daum.com";
    private static final String name = "가드릭";
    private static final String nickname = "godric";
    private static final String picture = "https://www.balladang.com";



    @Autowired
    private AccountService accountService;



    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private JobCategoryService jobCategoryService;
    
    @Autowired
    private JobCategoryRepo jobCategoryRepo;
    
    @Autowired
    private RegionRepo regionRepo;
    
    @Autowired
    private RegionService regionService;
    
    @Autowired
    private SchoolRepo schoolRepo;


    @BeforeEach
    public void beforeEach() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
//                .addFilter(springSecurityFilterChain)
//                .build();
        setJob();
        setRegion();
        setSchool();


    }

    private void setSchool() {
        schoolRepo.save(new School(
                "schoolName",
                "establishmentDate",
                "address",
                "schoolCode"
        ));
    }

    private void setRegion() {
        regionService.saveState("서울시");
        regionService.saveCity("서울시", "강남구");
    }

    @AfterEach
    public void afterEach() {
            jobCategoryRepo.deleteAll();
            schoolRepo.deleteAll();
            regionRepo.deleteAll();
    }

    @DisplayName("로컬 회원가입 테스트")
    @Test
    void localSignUp() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        LocalSignUpRequest localSignUpRequest = new LocalSignUpRequest(
                email,
                picture,
                nickname,
                name,
                phoneNum
        );

        mockMvc.perform(
                post("/api/v1/user/local/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(localSignUpRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                        .andExpect(status().isOk()).andDo(print());

    }


    @DisplayName("2단계 회원가입 테스트")
    @WithUserDetails("test@naver.com")
    @Test
    void secondarySignUp() throws Exception {
        Long id = addAccount();
        AccountSecondarySignUpRequest requestDto = AccountSecondarySignUpRequest.builder()
                .city("강남구")
                .job("백엔드 개발자")
                .graduationYear(2002)
                .name("김철수")
                .nickname("godric")
                .schoolCode("schoolCode")
                .State("서울시")
                .build();


        mockMvc.perform(
                        post("/api/v1/user/secondarysignup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk())
                .andDo(print());

    }

    private void setJob() {
        JobCategory jobCategory = new JobCategory("백엔드 개발자");
        JobCategory parent = new JobCategory("IT 직군");
        jobCategoryService.save(parent);
        jobCategory.setParent(parent);
        jobCategoryService.save(jobCategory);
    }

    private Long addAccount() {
        ObjectMapper objectMapper = new ObjectMapper();

        LocalSignUpRequest request = new LocalSignUpRequest(
                email,
                picture,
                nickname,
                name,
                phoneNum
        );

        Long id = accountService.localSignUp(request);
        return id;
    }


}