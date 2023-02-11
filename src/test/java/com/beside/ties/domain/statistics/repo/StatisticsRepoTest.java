package com.beside.ties.domain.statistics.repo;

import com.beside.ties.domain.common.dto.StatisticsDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@DisplayName("통계 레포 테스트")
@ActiveProfiles("test")
@SpringBootTest
class StatisticsRepoTest {

    @Autowired
    private StatisticsRepo statisticsRepo;

    @Test
    void regionStatistics() {
        List<StatisticsDto> statisticsDtos = statisticsRepo.regionStatistics(2L, 2002);
        for (StatisticsDto statisticsDto : statisticsDtos) {
            System.out.println("title = " + statisticsDto.getTitle());
            System.out.println("value = " + statisticsDto.getValue());
        }
    }

    @Test
    void jobStatistics() {
        List<StatisticsDto> statisticsDtos = statisticsRepo.jobStatistics(2L, 2002);
        for (StatisticsDto statisticsDto : statisticsDtos) {
            System.out.println("title = " + statisticsDto.getTitle());
            System.out.println("value = " + statisticsDto.getValue());
        }
    }

    @Test
    void mbtiStatistics() {
        List<StatisticsDto> statisticsDtos = statisticsRepo.mbtiStatistics(2L, 2002);
        for (StatisticsDto statisticsDto : statisticsDtos) {
            System.out.println("title = " + statisticsDto.getTitle());
            System.out.println("value = " + statisticsDto.getValue());
        }
    }
}