package com.beside.ties.domain.schoolregion.api;

import com.beside.ties.domain.common.dto.StatisticsDto;
import com.beside.ties.domain.common.vo.ResponseVo;
import com.beside.ties.domain.common.vo.StatisticsVo;
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

@Api(tags = "학교 지역 통계 API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/schoolRegion")
@RestController
public class SchoolRegionApi {

    private final SchoolRegionService schoolRegionService;

    @Operation(summary = "학교 직업 통계")
    @GetMapping("/{schoolId}")
    public ResponseEntity<?> schoolRegionStatistics(@PathVariable Long schoolId) {

        Long totalCount = schoolRegionService.totalCountBySchoolId(schoolId);
        List<SchoolRegion> schoolRegionTop4 = schoolRegionService.countTop4BySchoolId(schoolId);
        List<SchoolRegion> schoolRegions = schoolRegionService.findAllBySchoolId(schoolId);

        List<Long> top5PercentList = schoolRegionService.convertTop5Percent(schoolRegionTop4, totalCount);
        List<StatisticsDto> statisticsList = schoolRegionService.convertStatisticsDto(schoolRegions, totalCount);

        return ResponseEntity.ok().body(
                new ResponseVo(
                        new StatisticsVo(top5PercentList, statisticsList)
                ));
    }
}
