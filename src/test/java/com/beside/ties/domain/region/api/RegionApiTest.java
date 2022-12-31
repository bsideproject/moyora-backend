package com.beside.ties.domain.region.api;

import com.beside.ties.domain.jobcategory.service.JobCategoryService;
import com.beside.ties.domain.region.repo.RegionRepo;
import com.beside.ties.domain.region.service.RegionService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("지역 API 테스트")
@ActiveProfiles("test")
@SpringBootTest
class RegionApiTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private RegionService regionService;

    @Autowired
    private RegionRepo regionRepo;

    private MockMvc mockMvc;

    @Autowired
    private Filter springSecurityFilterChain;


    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(springSecurityFilterChain)
                .build();
        regionService.saveState("Gyeongsangnam-do");
        regionService.saveCity("Gyeongsangnam-do","ChangwonCity");
    }

    @AfterEach
    void cleanup(){
        regionRepo.deleteAll();
    }

    @DisplayName("모든 -도 가져오기")
    @Test
    void findAllStates() throws Exception {
        mockMvc.perform(
               get("/api/v1/region/state")
                       .characterEncoding(StandardCharsets.UTF_8)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Gyeongsangnam-do")))
                .andDo(print());
    }

    @DisplayName("특정 -도에 해당되는 모든 도시 가져오기")
    @Test
    void findCitiesByState() throws Exception {
        mockMvc.perform(
                        get("/api/v1/region/city")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .param("state","Gyeongsangnam-do")
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ChangwonCity")))
                .andDo(print());
    }


}