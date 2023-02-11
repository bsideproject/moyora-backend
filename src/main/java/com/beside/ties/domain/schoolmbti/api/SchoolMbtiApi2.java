package com.beside.ties.domain.schoolmbti.api;

import com.beside.ties.domain.common.dto.StatisticsDto;
import com.beside.ties.domain.common.dto.StatisticsRequestDto;
import com.beside.ties.domain.common.vo.StatisticsVo;
import com.beside.ties.domain.schoolmbti.entity.SchoolMbti;
import com.beside.ties.domain.schoolmbti.service.SchoolMbtiService;
import com.beside.ties.domain.statistics.service.StatisticsService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "학교 Mbti 통계 API (version 2)")
@RequiredArgsConstructor
@RequestMapping("/api/v1/schoolMbti")
@RestController
public class SchoolMbtiApi2 {

    private final StatisticsService statisticsService;

    @Operation(summary = "학교 Mbti 통계 (version 2)")
    @GetMapping("/statistics")
    public ResponseEntity<StatisticsVo> schoolMbtiStatistics(StatisticsRequestDto statisticsRequestDto) {

        List<StatisticsDto> statisticsList = statisticsService.mbtiStatistics(
                statisticsRequestDto.getSchoolId(),
                statisticsRequestDto.getGraduationYear().intValue());

        List<Long> top5PercentList = statisticsService.convertTop5Percent(statisticsList);
        statisticsService.convertAllPercent(statisticsList);

        return ResponseEntity.ok().body(new StatisticsVo(top5PercentList, statisticsList));
    }
}
