package com.beside.ties.domain.schoolregion.api;

import com.beside.ties.domain.BaseMvcTest;
import com.beside.ties.domain.region.entity.Region;
import com.beside.ties.domain.region.repo.RegionRepo;
import com.beside.ties.domain.school.entity.School;
import com.beside.ties.domain.school.service.SchoolService;
import com.beside.ties.domain.schoolregion.entity.SchoolRegion;
import com.beside.ties.domain.schoolregion.service.SchoolRegionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("학교 지역 통계 테스트")
@SpringBootTest
class SchoolRegionApiTest extends BaseMvcTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private Filter springSecurityFilterChain;


    @Autowired
    private SchoolRegionService schoolRegionService;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private RegionRepo regionRepo;

    private School searchSchool;

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

        String parent = "경상남도";
        String child1 = "창원시";
        String child2 = "밀양시";
        String child3 = "양산시";
        String child4 = "사천시";
        String child5 = "거제시";

        Region regionParent = new Region(parent);
        regionRepo.save(regionParent);

        Region regionChild1 = new Region(child1, regionParent);
        Region regionChild2 = new Region(child2, regionParent);
        Region regionChild3 = new Region(child3, regionParent);
        Region regionChild4 = new Region(child4, regionParent);
        Region regionChild5 = new Region(child5, regionParent);
        regionRepo.save(regionChild1);
        regionRepo.save(regionChild2);
        regionRepo.save(regionChild3);
        regionRepo.save(regionChild4);
        regionRepo.save(regionChild5);

        List<Region> regions = regionRepo.findByParentOrderByNameAsc(regionParent);

        SchoolRegion schoolRegion1 = SchoolRegion.createSchoolRegion(regions.get(0), searchSchool, 5L, 2002L);
        SchoolRegion schoolRegion2 = SchoolRegion.createSchoolRegion(regions.get(1), searchSchool, 1L, 2002L);
        SchoolRegion schoolRegion3 = SchoolRegion.createSchoolRegion(regions.get(2), searchSchool, 8L, 2003L);
        SchoolRegion schoolRegion4 = SchoolRegion.createSchoolRegion(regions.get(3), searchSchool, 10L, 2003L);
        SchoolRegion schoolRegion5 = SchoolRegion.createSchoolRegion(regions.get(4), searchSchool, 15L, 2002L);

        schoolRegionService.save(schoolRegion1);
        schoolRegionService.save(schoolRegion2);
        schoolRegionService.save(schoolRegion3);
        schoolRegionService.save(schoolRegion4);
        schoolRegionService.save(schoolRegion5);
    }

    @Test
    void schoolRegionStatistics() throws Exception {
        mockMvc.perform(
                        get("/api/v1/schoolRegion/" + searchSchool.getId())
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
}