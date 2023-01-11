package com.beside.ties.domain.schooljob.service;

import com.beside.ties.domain.common.dto.StatisticsDto;
import com.beside.ties.domain.schooljob.entity.SchoolJob;
import com.beside.ties.domain.schooljob.repo.SchoolJobRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class SchoolJobService {

    private final SchoolJobRepo schoolJobRepo;

    public List<SchoolJob> countTop4BySchoolId(Long schoolId) {
        return schoolJobRepo.findTop4BySchool_IdOrderByCountDesc(schoolId);
    }

    public Long totalCountBySchoolId(Long schoolId) {
        return schoolJobRepo.totalCountBySchoolId(schoolId);
    }

    public List<SchoolJob> findAllBySchoolId(Long schoolId) {
        return schoolJobRepo.findAllBySchool_Id(schoolId);
    }

    public Long save(SchoolJob schoolJob) {
        SchoolJob saveSchoolJob = schoolJobRepo.save(schoolJob);
        return saveSchoolJob.getId();
    }

    public SchoolJob findBySchoolIdAndRegionId(Long schoolId, Long regionId) {
        return schoolJobRepo.findBySchool_IdAndJobCategory_Id(schoolId, regionId)
                .orElseThrow(() -> new IllegalArgumentException("SchoolJob doesn't exist"));
    }

    public void countPlusOneUpdate(SchoolJob schoolJob) {
        schoolJob.plusOneCount();
    }

    public List<StatisticsDto> convertStatisticsDto(List<SchoolJob> schoolJobs, Long totalCount) {
        return schoolJobs.stream()
                .map(item ->
                        new StatisticsDto(
                                item.getJobCategory().getParent().getName() + " " + item.getJobCategory().getName(),
                                percentCalculate(item.getCount(), totalCount)))
                .collect(Collectors.toList());
    }

    public List<Long> convertTop5Percent(List<SchoolJob> schoolRegions, Long totalCount) {
        List<Long> top5Percent = schoolRegions.stream()
                .map(item -> percentCalculate(item.getCount(), totalCount))
                .collect(Collectors.toList());

        top5Percent.add(100L-top5Percent.stream().mapToLong(Long::longValue).sum());

        return top5Percent;
    }

    private Long percentCalculate(Long count, Long totalCount) {
        return (long)Math.floor((double) count / (double) totalCount * 100.0);
    }

    public void deleteAllInBatch() {
        schoolJobRepo.deleteAllInBatch();
    }
}
