package com.beside.ties.domain;

import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.account.repo.AccountRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ActiveProfiles("h2")
@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class BaseMvcTest {

    public static String testEmail = "test@naver.com";
    public static Account testMember;
    public static ObjectMapper objectMapper = new ObjectMapper();
    private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    AccountRepo accountRepo;

    public MockMvc mockMvc;

    @Autowired
    WebApplicationContext ctx;

    void applyEncodingFilter(){
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.ctx)
                .apply(springSecurity())
                .addFilters(new CharacterEncodingFilter("UTF-8", true)).build();
    }

    @BeforeEach
    void setUp(){
        applyEncodingFilter();
    }

    @BeforeTransaction
    void setUpAccount() {
        Account account = Account.builder()
                .email(testEmail)
                .kakaoId(testEmail)
                .nickname("nickname")
                .phoneNum("01084554545")
                .profile("dsfhiufhuw.rl")
                .name("goddirc")
                .build();

        account.updatePassword(passwordEncoder.encode("213"));
        testMember = accountRepo.save(account);
    }

    @AfterTransaction
    void cleanUpAccount(){
        accountRepo.delete(accountRepo.findAccountByEmail(testEmail).get());
    }


}
