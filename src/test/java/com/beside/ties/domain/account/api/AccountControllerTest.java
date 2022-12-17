package com.beside.ties.domain.account.api;

import com.beside.ties.domain.account.dto.request.AccountUpdateRequest;
import com.beside.ties.domain.account.dto.request.LocalSignUpRequest;
import com.beside.ties.domain.account.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.Filter;
import java.nio.charset.StandardCharsets;


@ActiveProfiles("test")
@SpringBootTest
class AccountControllerTest {

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

}