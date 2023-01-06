package com.beside.ties.domain.schoolguestbook.api;

import com.beside.ties.domain.BaseMvcTest;
import com.beside.ties.domain.account.dto.request.LocalSignUpRequest;
import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.account.service.AccountService;
import com.beside.ties.domain.school.entity.School;
import com.beside.ties.domain.school.service.SchoolService;
import com.beside.ties.domain.schoolguestbook.dto.SchoolGuestBookAddDto;
import com.beside.ties.domain.schoolguestbook.dto.SchoolGuestBookUpdateDto;
import com.beside.ties.domain.schoolguestbook.entity.SchoolGuestBook;
import com.beside.ties.domain.schoolguestbook.service.SchoolGuestBookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("학교 방명록 api 로직 테스트")
@ActiveProfiles("test")
@SpringBootTest
class SchoolGuestBookApiTest extends BaseMvcTest {

    private static final String phoneNum = "10188888888";
    private static final String email = "GODRI321C@daum.com";
    private static final String name = "가드릭";
    private static final String nickname = "godric";
    private static final String picture = "https://www.balladang.com";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private SchoolGuestBookService schoolGuestBookService;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private AccountService accountService;

    @BeforeEach
    public void beforeEach() {

        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(springSecurityFilterChain)
                .build();

        LocalSignUpRequest request = new LocalSignUpRequest(
                email,
                picture,
                nickname,
                name,
                phoneNum
        );

        accountService.localSignUp(request);

        Account account = accountService.loadUserByUsername(email);

        School school = new School("테스트학교", "2022-10-11", "테스트주소", "테스트코드");
        School school1 = new School("테스트학교1", "2022-10-12", "테스트주소1", "테스트코드1");
        School school2 = new School("테스트학교2", "2022-10-13", "테스트주소2", "테스트코드2");
        schoolService.save(school);
        schoolService.save(school1);
        schoolService.save(school2);

        SchoolGuestBook schoolGuestBook1 = new SchoolGuestBook(school, account, "우리학교 짱1");
        SchoolGuestBook schoolGuestBook2 = new SchoolGuestBook(school, account, "우리학교 짱2");
        SchoolGuestBook schoolGuestBook3 = new SchoolGuestBook(school, account, "우리학교 짱3");

        schoolGuestBookService.save(schoolGuestBook1);
        schoolGuestBookService.save(schoolGuestBook2);
        schoolGuestBookService.save(schoolGuestBook3);
    }

    @AfterEach
    public void afterEach() {
        schoolGuestBookService.deleteAllInBatch();
        schoolService.deleteAllInBatch();
        accountService.deleteAllInBatch();
    }

    @Test
    void findAllApiTest() throws Exception {
        mockMvc.perform(
                        get("/api/v1/schoolGuestBook/1")
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void saveApiTest() throws Exception {
        String content = "우리학교 짱4";

        SchoolGuestBookAddDto schoolGuestBookAddDto = new SchoolGuestBookAddDto(1L, content);

        mockMvc.perform(
                        post("/api/v1/schoolGuestBook/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(schoolGuestBookAddDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void updateApiTest() throws Exception {
        String content = "우리학교 짱10";
        SchoolGuestBookUpdateDto guestBookUpdateDto = new SchoolGuestBookUpdateDto(1L, content);

        mockMvc.perform(
                        put("/api/v1/schoolGuestBook/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(guestBookUpdateDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void deleteApiTest() throws Exception {

        mockMvc.perform(
                        delete("/api/v1/schoolGuestBook/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
}