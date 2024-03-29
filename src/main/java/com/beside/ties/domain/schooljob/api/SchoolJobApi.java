package com.beside.ties.domain.schooljob.api;

import com.beside.ties.domain.common.dto.StatisticsDto;
import com.beside.ties.domain.common.dto.StatisticsRequestDto;
import com.beside.ties.domain.common.vo.ResponseVo;
import com.beside.ties.domain.common.vo.StatisticsVo;
import com.beside.ties.domain.school.service.SchoolService;
import com.beside.ties.domain.schooljob.entity.SchoolJob;
import com.beside.ties.domain.schooljob.service.SchoolJobService;
import com.beside.ties.domain.schoolregion.entity.SchoolRegion;
import com.beside.ties.domain.schoolregion.service.SchoolRegionService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "학교 직업 통계 API")
@RequiredArgsConstructor
@RequestMapping("/api/v2/schoolJob")
@RestController
public class SchoolJobApi {

    private final SchoolJobService schoolJobService;

    @Operation(summary = "학교 직업 통계")
    @GetMapping("/statistics")
    public ResponseEntity<StatisticsVo> schoolJobStatistics(StatisticsRequestDto statisticsRequestDto) {

        Long totalCount = schoolJobService.totalCountBySchoolId(statisticsRequestDto);
        List<SchoolJob> schoolJobTop4 = schoolJobService.countTop4BySchoolId(statisticsRequestDto);
        List<SchoolJob> schoolJobs = schoolJobService.findAllBySchoolId(statisticsRequestDto);

        List<Long> top5PercentList = schoolJobService.convertTop5Percent(schoolJobTop4, totalCount);
        List<StatisticsDto> statisticsList = schoolJobService.convertStatisticsDto(schoolJobs, totalCount);

        return ResponseEntity.ok().body(new StatisticsVo(top5PercentList, statisticsList));
    }
}
