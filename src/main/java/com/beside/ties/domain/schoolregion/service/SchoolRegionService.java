package com.beside.ties.domain.schoolregion.service;

import com.beside.ties.domain.common.dto.StatisticsDto;
import com.beside.ties.domain.region.entity.Region;
import com.beside.ties.domain.school.entity.School;
import com.beside.ties.domain.schoolregion.entity.SchoolRegion;
import com.beside.ties.domain.schoolregion.repo.SchoolRegionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class SchoolRegionService {
    private final SchoolRegionRepo schoolRegionRepo;

    public List<SchoolRegion> countTop4BySchoolId(Long schoolId) {
        return schoolRegionRepo.findTop4BySchool_IdOrderByCountDesc(schoolId);
    }

    public Long totalCountBySchoolId(Long schoolId) {
        return schoolRegionRepo.totalCountBySchoolId(schoolId);
    }

    public List<SchoolRegion> findAllBySchoolId(Long schoolId) {
        return schoolRegionRepo.findAllBySchool_Id(schoolId);
    }

    public Long save(SchoolRegion schoolRegion) {
        SchoolRegion savedSchoolRegion = schoolRegionRepo.save(schoolRegion);
        return savedSchoolRegion.getId();
    }

    public SchoolRegion findBySchoolIdAndRegionId(Long schoolId, Long regionId) {
        return schoolRegionRepo.findBySchool_IdAndRegion_Id(schoolId, regionId)
                .orElseThrow(() -> new IllegalArgumentException("SchoolRegion doesn't exist"));
    }

    public void countPlusOneUpdate(SchoolRegion schoolRegion) {
        schoolRegion.plusOneCount();
    }

    public void countPlus(School school, Region region) {
        Optional<SchoolRegion> optionalSchoolRegion = schoolRegionRepo.findBySchool_IdAndRegion_Id(school.getId(), region.getId());
        if(optionalSchoolRegion.isPresent()){
            optionalSchoolRegion.get().plusOneCount();
        }else{
            schoolRegionRepo.save(new SchoolRegion(region, school));
        }
    }



    public List<StatisticsDto> convertStatisticsDto(List<SchoolRegion> schoolRegions, Long totalCount) {
        return schoolRegions.stream()
                .map(item ->
                        new StatisticsDto(
                                item.getRegion().getParent().getName() + " " + item.getRegion().getName(),
                                percentCalculate(item.getCount(), totalCount)))
                .collect(Collectors.toList());
    }

    public List<Long> convertTop5Percent(List<SchoolRegion> schoolRegions, Long totalCount) {
        List<Long> top5Percent = schoolRegions.stream()
                .map(item -> percentCalculate(item.getCount(), totalCount))
                .collect(Collectors.toList());

        top5Percent.add(100L-top5Percent.stream().mapToLong(Long::longValue).sum() == 100L ?
                0L : 100L-top5Percent.stream().mapToLong(Long::longValue).sum());

        return top5Percent;
    }

    private Long percentCalculate(Long count, Long totalCount) {
        return (long)Math.floor((double) count / (double) totalCount * 100.0);
    }

    public void deleteAllInBatch() {
        schoolRegionRepo.deleteAllInBatch();
    }
}
