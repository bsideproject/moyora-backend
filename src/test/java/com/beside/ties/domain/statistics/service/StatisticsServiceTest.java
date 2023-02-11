package com.beside.ties.domain.statistics.service;

import com.beside.ties.domain.common.dto.StatisticsDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("통계 서비스 로직 테스트")
@ActiveProfiles("test")
@SpringBootTest
class StatisticsServiceTest {
    @Autowired
    private StatisticsService statisticsService;

    @Test
    void convertTop5() {
        List<StatisticsDto> statisticsDtos = statisticsService.regionStatistics(2L, 2002);

        for (StatisticsDto statisticsDto : statisticsDtos) {
            System.out.println("title = " + statisticsDto.getTitle());
            System.out.println("value = " + statisticsDto.getValue());
        }

        List<Long> longs = statisticsService.convertTop5Percent(statisticsDtos);

        for (Long aLong : longs) {
            System.out.println("aLong = " + aLong);
        }
    }

}