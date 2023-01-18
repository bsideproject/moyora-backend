package com.beside.ties.domain.school.api;

import com.beside.ties.domain.BaseMvcTest;
import com.beside.ties.domain.school.entity.School;
import com.beside.ties.domain.school.service.SchoolService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("학교 API 테스트")
@ActiveProfiles("test")
@SpringBootTest
class SchoolApiTest extends BaseMvcTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private Filter springSecurityFilterChain;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(springSecurityFilterChain)
                .build();

        School school = new School("테스트학교", "2022-10-11", "테스트주소", "테스트코드");
        School school1 = new School("테스트학교1", "2022-10-12", "테스트주소1", "테스트코드1");
        School school2 = new School("테스트학교2", "2022-10-13", "테스트주소2", "테스트코드2");
        schoolService.save(school);
        schoolService.save(school1);
        schoolService.save(school2);

    }

    @AfterEach
    public void afterEach() {
        schoolService.deleteAllInBatch();
    }

    @DisplayName("전체 학교 가져오기")
    @Test
    void findAllSchool() throws Exception {
        mockMvc.perform(
                        get("/api/v1/school/")
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Disabled
    @DisplayName("특정 학교 이름으로 조회")
    @Test
    void findSchoolByName() throws Exception {
        String name = "테스트학교";
        mockMvc.perform(
                        get("/api/v1/school/schoolName")
                                .param("schoolName",name)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Disabled
    @DisplayName("특정 학교 가져오기")
    @Test
    void findSchoolById() throws Exception {
        Long id = 1L;
        mockMvc.perform(
                        get("/api/v1/school/" + id)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk())
                .andDo(print());

    }

}