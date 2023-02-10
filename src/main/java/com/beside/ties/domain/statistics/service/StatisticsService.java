package com.beside.ties.domain.statistics.service;

import com.beside.ties.domain.common.dto.StatisticsDto;
import com.beside.ties.domain.statistics.repo.StatisticsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class StatisticsService {

    private final StatisticsRepo statisticsRepo;

    public List<StatisticsDto> regionStatistics(Long schoolId, int graduationYear) {
        return statisticsRepo.regionStatistics(schoolId, graduationYear);
    }
    public List<StatisticsDto> jobStatistics(Long schoolId, int graduationYear) {
        return statisticsRepo.jobStatistics(schoolId, graduationYear);
    }
    public List<StatisticsDto> mbtiStatistics(Long schoolId, int graduationYear) {
        return statisticsRepo.mbtiStatistics(schoolId, graduationYear);
    }

    public List<Long> convertTop5Percent(List<StatisticsDto> statisticsList) {
        long totalCount = statisticsList.stream().mapToLong(item -> item.getValue()).sum();

        List<Long> top5Percent = statisticsList.stream()
                .map(item -> percentCalculate(item.getValue(), totalCount))
                .collect(Collectors.toList());

        if (statisticsList.size() > 4) {
            top5Percent.add(100L-top5Percent.stream().mapToLong(Long::longValue).sum() == 100L ?
                    0L : 100L-top5Percent.stream().mapToLong(Long::longValue).sum());
        }

        return top5Percent;
    }

    private Long percentCalculate(Long count, Long totalCount) {
        return (long)Math.floor((double) count / (double) totalCount * 100.0);
    }
}
