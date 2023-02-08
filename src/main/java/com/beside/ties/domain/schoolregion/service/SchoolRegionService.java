package com.beside.ties.domain.schoolregion.service;

import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.common.dto.StatisticsDto;
import com.beside.ties.domain.common.dto.StatisticsRequestDto;
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

    public List<SchoolRegion> countTop4BySchoolId(StatisticsRequestDto statisticsRequestDto) {
        return schoolRegionRepo.findTop4BySchool_IdAndGraduationYearOrderByCountDesc(
                statisticsRequestDto.getSchoolId(),
                statisticsRequestDto.getGraduationYear());
    }

    public Long totalCountBySchoolId(StatisticsRequestDto statisticsRequestDto) {
        return schoolRegionRepo.totalCountBySchoolId(
                statisticsRequestDto.getSchoolId(),
                statisticsRequestDto.getGraduationYear());
    }

    public List<SchoolRegion> findAllBySchoolId(StatisticsRequestDto statisticsRequestDto) {
        return schoolRegionRepo.findAllBySchool_Id(
                statisticsRequestDto.getSchoolId(),
                statisticsRequestDto.getGraduationYear());
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

    public void countPlus(Account account, Region region) {
        Optional<SchoolRegion> optionalSchoolRegion = schoolRegionRepo.findBySchoolAndRegionAndGraduationYear(account.getSchool(), region, Long.valueOf(account.getGraduationYear()));
        if(optionalSchoolRegion.isPresent()){
            optionalSchoolRegion.get().plusOneCount();
        }else{
            schoolRegionRepo.save(new SchoolRegion(region, account.getSchool(), 1L, Long.valueOf(account.getGraduationYear())));
        }
    }

    public void countPlus(Account account, Region region, School school, Long graduationYear) {
        Optional<SchoolRegion> optionalSchoolRegion = schoolRegionRepo.findBySchoolAndRegionAndGraduationYear(school, region, graduationYear);
        if(optionalSchoolRegion.isPresent()){
            optionalSchoolRegion.get().plusOneCount();
        }else{
            schoolRegionRepo.save(new SchoolRegion(region, school, 1L, graduationYear));
        }
    }

    public void countMinus(Account account, Region region) {
        Optional<SchoolRegion> optionalSchoolRegion = schoolRegionRepo.findBySchoolAndRegionAndGraduationYear(account.getSchool(), region, Long.valueOf(account.getGraduationYear()));
        if(optionalSchoolRegion.isPresent()){
            optionalSchoolRegion.get().minusOneCount();
        }else{
            schoolRegionRepo.save(new SchoolRegion(region, account.getSchool(), 0L, Long.valueOf(account.getGraduationYear())));
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

        if (schoolRegions.size() > 4) {
            top5Percent.add(100L-top5Percent.stream().mapToLong(Long::longValue).sum() == 100L ?
                    0L : 100L-top5Percent.stream().mapToLong(Long::longValue).sum());
        }


        return top5Percent;
    }

    private Long percentCalculate(Long count, Long totalCount) {
        return (long)Math.floor((double) count / (double) totalCount * 100.0);
    }

    public void deleteAllInBatch() {
        schoolRegionRepo.deleteAllInBatch();
    }
}
