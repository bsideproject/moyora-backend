package com.beside.ties.domain.schooljob.service;

import com.beside.ties.domain.common.dto.StatisticsDto;
import com.beside.ties.domain.jobcategory.entity.JobCategory;
import com.beside.ties.domain.jobcategory.repo.JobCategoryRepo;
import com.beside.ties.domain.school.entity.School;
import com.beside.ties.domain.school.service.SchoolService;
import com.beside.ties.domain.schooljob.entity.SchoolJob;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("학교 직업 서비스 로직 테스트")
@ActiveProfiles("test")
@SpringBootTest
class SchoolJobServiceTest {
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

        School school = new School("테스트학교", "2022-10-11", "테스트주소", "테스트코드");
        School school1 = new School("테스트학교1", "2022-10-12", "테스트주소1", "테스트코드1");
        School school2 = new School("테스트학교2", "2022-10-13", "테스트주소2", "테스트코드2");
        schoolService.save(school);
        schoolService.save(school1);
        schoolService.save(school2);

        searchSchool = schoolService.findBySchoolName("테스트학교").get(0);

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

    @AfterEach
    public void afterEach() {
        schoolJobService.deleteAllInBatch();
        schoolService.deleteAllInBatch();
        jobCategoryRepo.deleteAllInBatch();
    }

    @DisplayName("학교 별 직업 count 총합 조회")
    @Test
    void totalCountBySchoolId() {
        Long totalCount = schoolJobService.totalCountBySchoolId(searchSchool.getId());

        assertThat(totalCount).isEqualTo(39L);
    }

    @DisplayName("학교 별 지역 top4 조회")
    @Test
    void countTop4BySchoolId() {
        List<SchoolJob> schoolJobs = schoolJobService.countTop4BySchoolId(searchSchool.getId());

        assertThat(schoolJobs.size()).isEqualTo(4);
        assertThat(schoolJobs.get(0).getCount()).isEqualTo(15L);
        assertThat(schoolJobs.get(1).getCount()).isEqualTo(10L);
        assertThat(schoolJobs.get(2).getCount()).isEqualTo(8L);
        assertThat(schoolJobs.get(3).getCount()).isEqualTo(5L);
    }

    @DisplayName("학교 별 직업 전체 조회")
    @Test
    void findAllBySchoolId() {
        List<SchoolJob> schoolJobs = schoolJobService.findAllBySchoolId(searchSchool.getId());

        assertThat(schoolJobs.size()).isEqualTo(5);
    }

    @DisplayName("학교 별 직업 퍼센트")
    @Test
    void toPercent() {
        List<SchoolJob> schoolJobs = schoolJobService.findAllBySchoolId(searchSchool.getId());
        Long totalCount = schoolJobService.totalCountBySchoolId(searchSchool.getId());
        List<StatisticsDto> statisticsList = schoolJobService.convertStatisticsDto(schoolJobs, totalCount);

        assertThat(statisticsList.get(0).getTitle()).isEqualTo("의료 수의사");
        assertThat(statisticsList.get(1).getTitle()).isEqualTo("의료 의료직기타");
        assertThat(statisticsList.get(2).getTitle()).isEqualTo("의료 의료기사");
        assertThat(statisticsList.get(3).getTitle()).isEqualTo("의료 간호사");
        assertThat(statisticsList.get(4).getTitle()).isEqualTo("의료 간호조무사");
    }

    @DisplayName("학교 별 직업 top5 퍼센트")
    @Test
    void convertTop5Percent() {
        List<SchoolJob> schoolJobs = schoolJobService.countTop4BySchoolId(searchSchool.getId());
        Long totalCount = schoolJobService.totalCountBySchoolId(searchSchool.getId());
        List<Long> longs = schoolJobService.convertTop5Percent(schoolJobs, totalCount);
        for (Long aLong : longs) {
            System.out.println("aLong = " + aLong);
        }
    }

    @DisplayName("학교직업 학교id 와 직업id로 조회")
    @Test
    void findBySchool_IdAndRegion_Id() {
        SchoolJob schoolJob = schoolJobService.findBySchoolIdAndRegionId(searchSchool.getId(), searchJobCategory.getId());

        assertThat(schoolJob.getCount()).isEqualTo(5L);
    }

    @DisplayName("count 1 증가")
    @Test
    void countPlusOneUpdate() {
        SchoolJob schoolJob = schoolJobService.findBySchoolIdAndRegionId(searchSchool.getId(), searchJobCategory.getId());
        schoolJobService.countPlusOneUpdate(schoolJob);

        assertThat(schoolJob.getCount()).isEqualTo(6L);
    }
}