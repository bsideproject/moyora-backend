package com.beside.ties.domain.schoolmbti.service;

import com.beside.ties.domain.account.entity.MBTI;
import com.beside.ties.domain.common.dto.StatisticsDto;
import com.beside.ties.domain.jobcategory.entity.JobCategory;
import com.beside.ties.domain.mbti.entity.Mbti;
import com.beside.ties.domain.mbti.repo.MbtiRepo;
import com.beside.ties.domain.school.entity.School;
import com.beside.ties.domain.schooljob.entity.SchoolJob;
import com.beside.ties.domain.schoolmbti.entity.SchoolMbti;
import com.beside.ties.domain.schoolmbti.repo.SchoolMbtiRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class SchoolMbtiService {

    private final SchoolMbtiRepo schoolMbtiRepo;
    private final MbtiRepo mbtiRepo;

    public List<SchoolMbti> countTop4BySchoolId(Long schoolId) {
        return schoolMbtiRepo.findTop4BySchool_IdOrderByCountDesc(schoolId);
    }

    public Long totalCountBySchoolId(Long schoolId) {
        return schoolMbtiRepo.totalCountBySchoolId(schoolId);
    }

    public List<SchoolMbti> findAllBySchoolId(Long schoolId) {
        return schoolMbtiRepo.findAllBySchool_Id(schoolId);
    }

    public Long save(SchoolMbti schoolMbti) {
        SchoolMbti saveSchoolJob = schoolMbtiRepo.save(schoolMbti);
        return saveSchoolJob.getId();
    }

    public SchoolMbti findBySchoolIdAndMbtiId(Long schoolId, Long mbtiId) {
        return schoolMbtiRepo.findBySchool_IdAndMbti_Id(schoolId, mbtiId)
                .orElseThrow(() -> new IllegalArgumentException("SchoolJob doesn't exist"));
    }

    public void countPlusOneUpdate(SchoolMbti schoolMbti) {
        schoolMbti.plusOneCount();
    }

    public void countPlus(School school, String stringMbti) {
        Mbti mbti = mbtiRepo.findByName(stringMbti).orElseThrow(() -> new IllegalArgumentException("해당 엠비티아이가 존재하지 않습니다."));

        Optional<SchoolMbti> optionalSchoolMbti = schoolMbtiRepo.findBySchool_IdAndMbti_Id(school.getId(), mbti.getId());
        if(optionalSchoolMbti.isPresent()){
            optionalSchoolMbti.get().plusOneCount();
        }else{
                schoolMbtiRepo.save(new SchoolMbti(mbti, school));
        }
    }

    public void countMinus(School school, String stringMbti) {
        Mbti mbti = mbtiRepo.findByName(stringMbti).orElseThrow(() -> new IllegalArgumentException("해당 엠비티아이가 존재하지 않습니다."));

        Optional<SchoolMbti> optionalSchoolMbti = schoolMbtiRepo.findBySchool_IdAndMbti_Id(school.getId(), mbti.getId());
        if(optionalSchoolMbti.isPresent()){
            optionalSchoolMbti.get().minusOneCount();
        }else{
            schoolMbtiRepo.save(new SchoolMbti(mbti, school));
        }
    }



    public List<StatisticsDto> convertStatisticsDto(List<SchoolMbti> schoolMbties, Long totalCount) {
        return schoolMbties.stream()
                .map(item ->
                        new StatisticsDto(
                                item.getMbti().getName(),
                                percentCalculate(item.getCount(), totalCount)))
                .collect(Collectors.toList());
    }

    public List<Long> convertTop5Percent(List<SchoolMbti> schoolMbties, Long totalCount) {
        List<Long> top5Percent = schoolMbties.stream()
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
        schoolMbtiRepo.deleteAllInBatch();
    }
}
