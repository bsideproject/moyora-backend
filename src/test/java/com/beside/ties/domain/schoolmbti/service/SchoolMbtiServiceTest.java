package com.beside.ties.domain.schoolmbti.service;

import com.beside.ties.domain.common.dto.StatisticsDto;
import com.beside.ties.domain.common.dto.StatisticsRequestDto;
import com.beside.ties.domain.mbti.entity.Mbti;
import com.beside.ties.domain.mbti.repo.MbtiRepo;
import com.beside.ties.domain.school.entity.School;
import com.beside.ties.domain.school.service.SchoolService;
import com.beside.ties.domain.schoolmbti.entity.SchoolMbti;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("학교 MBTI 서비스 로직 테스트")
@ActiveProfiles("test")
@SpringBootTest
class SchoolMbtiServiceTest {
    @Autowired
    private SchoolMbtiService schoolMbtiService;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private MbtiRepo mbtiRepo;

    private School searchSchool;

    private Mbti searchMbti;

    private StatisticsRequestDto statisticsRequestDto;

    @BeforeEach
    public void beforeEach() {

        School school = new School("테스트학교", "2022-10-11", "테스트주소", "테스트코드");
        School school1 = new School("테스트학교1", "2022-10-12", "테스트주소1", "테스트코드1");
        School school2 = new School("테스트학교2", "2022-10-13", "테스트주소2", "테스트코드2");
        schoolService.save(school);
        schoolService.save(school1);
        schoolService.save(school2);

        searchSchool = schoolService.findBySchoolName("테스트학교").get(0);

        statisticsRequestDto = new StatisticsRequestDto(2002L, searchSchool.getId());

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

        SchoolMbti schoolMbti1 = new SchoolMbti(mbti1, searchSchool, 5L ,2002L);
        SchoolMbti schoolMbti2 = new SchoolMbti(mbti2, searchSchool, 1L, 2002L);
        SchoolMbti schoolMbti3 = new SchoolMbti(mbti3, searchSchool, 8L, 2003L);
        SchoolMbti schoolMbti4 = new SchoolMbti(mbti4, searchSchool, 10L, 2003L);
        SchoolMbti schoolMbti5 = new SchoolMbti(mbti5, searchSchool, 15L, 2002L);

        schoolMbtiService.save(schoolMbti1);
        schoolMbtiService.save(schoolMbti2);
        schoolMbtiService.save(schoolMbti3);
        schoolMbtiService.save(schoolMbti4);
        schoolMbtiService.save(schoolMbti5);
    }

    @AfterEach
    public void afterEach() {
        schoolMbtiService.deleteAllInBatch();
        schoolService.deleteAllInBatch();
        mbtiRepo.deleteAllInBatch();
    }

    @DisplayName("학교 별 Mbti count 총합 조회")
    @Test
    void totalCountBySchoolId() {
        Long totalCount = schoolMbtiService.totalCountBySchoolId(statisticsRequestDto);

        assertThat(totalCount).isEqualTo(21L);
    }

    @DisplayName("학교 별 Mbti top4 조회")
    @Test
    void countTop4BySchoolId() {
        List<SchoolMbti> schoolMbtis = schoolMbtiService.countTop4BySchoolId(statisticsRequestDto);

        assertThat(schoolMbtis.size()).isEqualTo(3);
        assertThat(schoolMbtis.get(0).getCount()).isEqualTo(15L);
        assertThat(schoolMbtis.get(1).getCount()).isEqualTo(5L);
    }

    @DisplayName("학교 별 Mbti 전체 조회")
    @Test
    void findAllBySchoolId() {
        List<SchoolMbti> schoolMbtis = schoolMbtiService.findAllBySchoolId(statisticsRequestDto);

        assertThat(schoolMbtis.size()).isEqualTo(3);
    }

    @DisplayName("학교 별 Mbti 퍼센트")
    @Test
    void toPercent() {
        List<SchoolMbti> schoolMbtis = schoolMbtiService.findAllBySchoolId(statisticsRequestDto);
        Long totalCount = schoolMbtiService.totalCountBySchoolId(statisticsRequestDto);
        List<StatisticsDto> statisticsList = schoolMbtiService.convertStatisticsDto(schoolMbtis, totalCount);

        assertThat(statisticsList.get(0).getTitle()).isEqualTo("ISFJ");
        assertThat(statisticsList.get(1).getTitle()).isEqualTo("ENTP");
    }

    @DisplayName("학교 별 Mbti top5 퍼센트")
    @Test
    void convertTop5Percent() {
        List<SchoolMbti> schoolMbtis = schoolMbtiService.countTop4BySchoolId(statisticsRequestDto);
        Long totalCount = schoolMbtiService.totalCountBySchoolId(statisticsRequestDto);
        List<Long> longs = schoolMbtiService.convertTop5Percent(schoolMbtis, totalCount);
        for (Long aLong : longs) {
            System.out.println("aLong = " + aLong);
        }
    }

    @DisplayName("학교직업 학교id 와 Mbtiid로 조회")
    @Test
    void findBySchool_IdAndRegion_Id() {
        SchoolMbti schoolMbti = schoolMbtiService.findBySchoolIdAndMbtiId(searchSchool.getId(), searchMbti.getId());

        assertThat(schoolMbti.getCount()).isEqualTo(5L);
    }

    @DisplayName("count 1 증가")
    @Test
    void countPlusOneUpdate() {
        SchoolMbti schoolMbti = schoolMbtiService.findBySchoolIdAndMbtiId(searchSchool.getId(), searchMbti.getId());
        schoolMbtiService.countPlusOneUpdate(schoolMbti);

        assertThat(schoolMbti.getCount()).isEqualTo(6L);
    }
}