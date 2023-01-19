package com.beside.ties.domain.schoolregion.service;

import com.beside.ties.domain.account.dto.request.LocalSignUpRequest;
import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.common.dto.StatisticsDto;
import com.beside.ties.domain.region.entity.Region;
import com.beside.ties.domain.region.repo.RegionRepo;
import com.beside.ties.domain.school.entity.School;
import com.beside.ties.domain.school.service.SchoolService;
import com.beside.ties.domain.schoolguestbook.entity.SchoolGuestBook;
import com.beside.ties.domain.schoolregion.entity.SchoolRegion;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("학교 지역 서비스 로직 테스트")
@ActiveProfiles("test")
@SpringBootTest
class SchoolRegionServiceTest {

    @Autowired
    private SchoolRegionService schoolRegionService;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private RegionRepo regionRepo;

    private School searchSchool;

    private Region searchRegion;

    @BeforeEach
    public void beforeEach() {

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
        Optional<Region> regionByName = regionRepo.findRegionByName(child1);

        searchRegion = regionByName.get();

        List<Region> regions = regionRepo.findRegionsByParent(regionParent);

        SchoolRegion schoolRegion1 = SchoolRegion.createSchoolRegion(regions.get(0), searchSchool, 5L);
        SchoolRegion schoolRegion2 = SchoolRegion.createSchoolRegion(regions.get(1), searchSchool, 1L);
        SchoolRegion schoolRegion3 = SchoolRegion.createSchoolRegion(regions.get(2), searchSchool, 8L);
        SchoolRegion schoolRegion4 = SchoolRegion.createSchoolRegion(regions.get(3), searchSchool, 10L);
        SchoolRegion schoolRegion5 = SchoolRegion.createSchoolRegion(regions.get(4), searchSchool, 15L);

        schoolRegionService.save(schoolRegion1);
        schoolRegionService.save(schoolRegion2);
        schoolRegionService.save(schoolRegion3);
        schoolRegionService.save(schoolRegion4);
        schoolRegionService.save(schoolRegion5);
    }

    @AfterEach
    public void afterEach() {
        schoolRegionService.deleteAllInBatch();
        schoolService.deleteAllInBatch();
        regionRepo.deleteAllInBatch();
    }

    @DisplayName("학교 별 지역 count 총합 조회")
    @Test
    void totalCountBySchoolId() {
        Long totalCount = schoolRegionService.totalCountBySchoolId(searchSchool.getId());

        assertThat(totalCount).isEqualTo(39L);
    }

    @DisplayName("학교 별 지역 top4 조회")
    @Test
    void countTop4BySchoolId() {
        List<SchoolRegion> schoolRegions = schoolRegionService.countTop4BySchoolId(searchSchool.getId());

        assertThat(schoolRegions.size()).isEqualTo(4);
        assertThat(schoolRegions.get(0).getCount()).isEqualTo(15L);
        assertThat(schoolRegions.get(1).getCount()).isEqualTo(10L);
        assertThat(schoolRegions.get(2).getCount()).isEqualTo(8L);
        assertThat(schoolRegions.get(3).getCount()).isEqualTo(5L);
    }

    @DisplayName("학교 별 지역 전체 조회")
    @Test
    void findAllBySchoolId() {
        List<SchoolRegion> schoolRegions = schoolRegionService.findAllBySchoolId(searchSchool.getId());

        assertThat(schoolRegions.size()).isEqualTo(5);
    }

    @DisplayName("학교 별 지역 퍼센트")
    @Test
    void toPercent() {
        List<SchoolRegion> schoolRegions = schoolRegionService.findAllBySchoolId(searchSchool.getId());
        Long totalCount = schoolRegionService.totalCountBySchoolId(searchSchool.getId());
        List<StatisticsDto> statisticsList = schoolRegionService.convertStatisticsDto(schoolRegions, totalCount);

        assertThat(statisticsList.get(0).getTitle()).isEqualTo("경상남도 거제시");
        assertThat(statisticsList.get(1).getTitle()).isEqualTo("경상남도 사천시");
        assertThat(statisticsList.get(2).getTitle()).isEqualTo("경상남도 양산시");
        assertThat(statisticsList.get(3).getTitle()).isEqualTo("경상남도 창원시");
        assertThat(statisticsList.get(4).getTitle()).isEqualTo("경상남도 밀양시");
    }

    @Disabled
    @DisplayName("학교 별 top5 퍼센트")
    @Test
    void convertTop5Percent() {
        List<SchoolRegion> schoolRegions = schoolRegionService.countTop4BySchoolId(searchSchool.getId());
        Long totalCount = schoolRegionService.totalCountBySchoolId(searchSchool.getId());
        List<Long> longs = schoolRegionService.convertTop5Percent(schoolRegions, totalCount);
        for (Long aLong : longs) {
            System.out.println("aLong = " + aLong);
        }
    }

    @DisplayName("학교지역 학교id 와 지역id로 조회")
    @Test
    void findBySchool_IdAndRegion_Id() {
        SchoolRegion schoolRegion = schoolRegionService.findBySchoolIdAndRegionId(searchSchool.getId(), searchRegion.getId());

        assertThat(schoolRegion.getCount()).isEqualTo(5L);
    }

    @DisplayName("count 1 증가")
    @Test
    void countPlusOneUpdate() {
        SchoolRegion schoolRegion = schoolRegionService.findBySchoolIdAndRegionId(searchSchool.getId(), searchRegion.getId());
        schoolRegionService.countPlusOneUpdate(schoolRegion);

        assertThat(schoolRegion.getCount()).isEqualTo(6L);
    }
}