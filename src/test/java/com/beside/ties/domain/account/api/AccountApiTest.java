package com.beside.ties.domain.account.api;

import com.beside.ties.domain.account.dto.request.AccountUpdateRequest;
import com.beside.ties.domain.account.dto.request.LocalSignUpRequest;
import com.beside.ties.domain.account.service.AccountService;
import com.beside.ties.domain.jobcategory.entity.JobCategory;
import com.beside.ties.domain.jobcategory.service.JobCategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.Filter;
import java.nio.charset.StandardCharsets;


@DisplayName("계정 API 테스트")
@ActiveProfiles("test")
@SpringBootTest
class AccountApiTest {

    private static final String phoneNum = "10188888888";
    private static final String email = "GODRI321C@daum.com";
    private static final String name = "가드릭";
    private static final String nickname = "godric";
    private static final String picture = "https://www.balladang.com";



    @Autowired
    private AccountService accountService;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private JobCategoryService jobCategoryService;


    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(springSecurityFilterChain)
                .build();
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


    @Disabled
    @DisplayName("2단계 회원가입 테스트")
    @Test
    void secondarySignUp() throws Exception {
        Long id = addAccount();
        JobCategory jobCategory = new JobCategory("백엔드 개발자");
        jobCategoryService.save(jobCategory);

        /*
        new AccountUpdateRequest(
                id,
                "nickname",
                "김철수",
                2002,
                jobCategory,
        )
        */


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