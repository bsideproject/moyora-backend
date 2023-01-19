package com.beside.ties.domain.schoolmbti.api;

import com.beside.ties.domain.BaseMvcTest;
import com.beside.ties.domain.mbti.entity.Mbti;
import com.beside.ties.domain.mbti.repo.MbtiRepo;
import com.beside.ties.domain.school.entity.School;
import com.beside.ties.domain.school.service.SchoolService;
import com.beside.ties.domain.schoolmbti.entity.SchoolMbti;
import com.beside.ties.domain.schoolmbti.service.SchoolMbtiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("학교 Mbti 통계 테스트")
@SpringBootTest
class SchoolMbtiApiTest extends BaseMvcTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private Filter springSecurityFilterChain;


    @Autowired
    private SchoolMbtiService schoolMbtiService;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private MbtiRepo mbtiRepo;

    private School searchSchool;

    private Mbti searchMbti;

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

        searchSchool = schoolService.findBySchoolName("테스트학교").get(0);

        Mbti mbti1 = new Mbti("ENTP");
        Mbti mbti2 = new Mbti("ENTJ");
        Mbti mbti3 = new Mbti("INTP");
        Mbti mbti4 = new Mbti("INTJ");
        Mbti mbti5 = new Mbti("ISFJ");

        mbtiRepo.save(mbti1);
        mbtiRepo.save(mbti2);
        mbtiRepo.save(mbti3);
        mbtiRepo.save(mbti4);
        mbtiRepo.save(mbti5);

        searchMbti = mbtiRepo.findByName("ENTP").get();

        SchoolMbti schoolMbti1 = new SchoolMbti(mbti1, searchSchool, 5L);
        SchoolMbti schoolMbti2 = new SchoolMbti(mbti2, searchSchool, 1L);
        SchoolMbti schoolMbti3 = new SchoolMbti(mbti3, searchSchool, 8L);
        SchoolMbti schoolMbti4 = new SchoolMbti(mbti4, searchSchool, 10L);
        SchoolMbti schoolMbti5 = new SchoolMbti(mbti5, searchSchool, 15L);

        schoolMbtiService.save(schoolMbti1);
        schoolMbtiService.save(schoolMbti2);
        schoolMbtiService.save(schoolMbti3);
        schoolMbtiService.save(schoolMbti4);
        schoolMbtiService.save(schoolMbti5);
    }

    @Disabled
    @Test
    void schoolMbtiStatistics() throws Exception {
        mockMvc.perform(
                        get("/api/v1/schoolMbti/" + searchSchool.getId())
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

}