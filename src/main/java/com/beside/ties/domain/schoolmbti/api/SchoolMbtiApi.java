package com.beside.ties.domain.schoolmbti.api;

import com.beside.ties.domain.common.dto.StatisticsDto;
import com.beside.ties.domain.common.dto.StatisticsRequestDto;
import com.beside.ties.domain.common.vo.ResponseVo;
import com.beside.ties.domain.common.vo.StatisticsVo;
import com.beside.ties.domain.schooljob.entity.SchoolJob;
import com.beside.ties.domain.schoolmbti.entity.SchoolMbti;
import com.beside.ties.domain.schoolmbti.service.SchoolMbtiService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "학교 Mbti 통계 API")
@RequiredArgsConstructor
@RequestMapping("/api/v2/schoolMbti")
@RestController
public class SchoolMbtiApi {

    private final SchoolMbtiService schoolMbtiService;

    @Operation(summary = "학교 Mbti 통계")
    @GetMapping("/statistics")
    public ResponseEntity<StatisticsVo> schoolMbtiStatistics(StatisticsRequestDto statisticsRequestDto) {

        Long totalCount = schoolMbtiService.totalCountBySchoolId(statisticsRequestDto);
        List<SchoolMbti> schoolMbtiTop4 = schoolMbtiService.countTop4BySchoolId(statisticsRequestDto);
        List<SchoolMbti> schoolMbtis = schoolMbtiService.findAllBySchoolId(statisticsRequestDto);

        List<Long> top5PercentList = schoolMbtiService.convertTop5Percent(schoolMbtiTop4, totalCount);
        List<StatisticsDto> statisticsList = schoolMbtiService.convertStatisticsDto(schoolMbtis, totalCount);

        return ResponseEntity.ok().body(new StatisticsVo(top5PercentList, statisticsList));
    }
}
