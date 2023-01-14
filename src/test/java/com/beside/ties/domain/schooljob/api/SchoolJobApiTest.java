package com.beside.ties.domain.schooljob.api;

import com.beside.ties.domain.BaseMvcTest;
import com.beside.ties.domain.jobcategory.entity.JobCategory;
import com.beside.ties.domain.jobcategory.repo.JobCategoryRepo;
import com.beside.ties.domain.school.entity.School;
import com.beside.ties.domain.school.service.SchoolService;
import com.beside.ties.domain.schooljob.entity.SchoolJob;
import com.beside.ties.domain.schooljob.service.SchoolJobService;
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

@DisplayName("학교 직업 통계 테스트")
@SpringBootTest
class SchoolJobApiTest extends BaseMvcTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private Filter springSecurityFilterChain;


    @Autowired
    private SchoolJobService schoolJobService;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private JobCategoryRepo jobCategoryRepo;

    private School searchSchool;

    private JobCategory searchJobCategory;

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

        searchSchool = schoolService.findBySchoolName("테스트학교");

        String parent = "의료";
        String child1 = "간호사";
        String child2 = "간호조무사";
        String child3 = "의료기사";
        String child4 = "의료직기타";
        String child5 = "수의사";

        JobCategory jobCategoryParent = new JobCategory(parent);
        jobCategoryRepo.save(jobCategoryParent);

        JobCategory jobCategory1 = new JobCategory(child1, jobCategoryParent);
        JobCategory jobCategory2 = new JobCategory(child2, jobCategoryParent);
        JobCategory jobCategory3 = new JobCategory(child3, jobCategoryParent);
        JobCategory jobCategory4 = new JobCategory(child4, jobCategoryParent);
        JobCategory jobCategory5 = new JobCategory(child5, jobCategoryParent);
        jobCategoryRepo.save(jobCategory1);
        jobCategoryRepo.save(jobCategory2);
        jobCategoryRepo.save(jobCategory3);
        jobCategoryRepo.save(jobCategory4);
        jobCategoryRepo.save(jobCategory5);

        searchJobCategory = jobCategoryRepo.findJobCategoryByName(child1).get();

        List<JobCategory> jobCategories = jobCategoryRepo.findAllByParent(jobCategoryParent);

        SchoolJob schoolJob1 = SchoolJob.createSchoolJob(jobCategories.get(0), searchSchool, 5L);
        SchoolJob schoolJob2 = SchoolJob.createSchoolJob(jobCategories.get(1), searchSchool, 1L);
        SchoolJob schoolJob3 = SchoolJob.createSchoolJob(jobCategories.get(2), searchSchool, 8L);
        SchoolJob schoolJob4 = SchoolJob.createSchoolJob(jobCategories.get(3), searchSchool, 10L);
        SchoolJob schoolJob5 = SchoolJob.createSchoolJob(jobCategories.get(4), searchSchool, 15L);

        schoolJobService.save(schoolJob1);
        schoolJobService.save(schoolJob2);
        schoolJobService.save(schoolJob3);
        schoolJobService.save(schoolJob4);
        schoolJobService.save(schoolJob5);
    }

    @Test
    void schoolRegionStatistics() throws Exception {
        mockMvc.perform(
                        get("/api/v1/schoolJob/" + searchSchool.getId())
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

}