package com.beside.ties.domain.schoolmbti.service;

import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.account.entity.MBTI;
import com.beside.ties.domain.common.dto.StatisticsDto;
import com.beside.ties.domain.common.dto.StatisticsRequestDto;
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

    public List<SchoolMbti> countTop4BySchoolId(StatisticsRequestDto statisticsRequestDto) {
        return schoolMbtiRepo.findTop4BySchool_IdAndGraduationYearOrderByCountDesc(
                statisticsRequestDto.getSchoolId(),
                statisticsRequestDto.getGraduationYear());
    }

    public Long totalCountBySchoolId(StatisticsRequestDto statisticsRequestDto) {
        return schoolMbtiRepo.totalCountBySchoolId(
                statisticsRequestDto.getSchoolId(),
                statisticsRequestDto.getGraduationYear());
    }

    public List<SchoolMbti> findAllBySchoolId(StatisticsRequestDto statisticsRequestDto) {
        return schoolMbtiRepo.findAllBySchool_Id(
                statisticsRequestDto.getSchoolId(),
                statisticsRequestDto.getGraduationYear());
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

    public void countPlus(Account account, String stringMbti) {
        if(account.getSchool() == null) return;
        Mbti mbti = mbtiRepo.findByName(stringMbti).orElseThrow(() -> new IllegalArgumentException("해당 엠비티아이가 존재하지 않습니다."));

        Optional<SchoolMbti> optionalSchoolMbti = schoolMbtiRepo.findBySchoolAndMbtiAndGraduationYear(account.getSchool(), mbti, Long.valueOf(account.getGraduationYear()));
        if(optionalSchoolMbti.isPresent()){
            optionalSchoolMbti.get().plusOneCount();
        }else{
                schoolMbtiRepo.save(new SchoolMbti(mbti, account.getSchool()));
        }
    }

    public void countMinus(Account account, String stringMbti) {
        if(account.getSchool() == null) return;
        Mbti mbti = mbtiRepo.findByName(stringMbti).orElseThrow(() -> new IllegalArgumentException("해당 엠비티아이가 존재하지 않습니다."));

        Optional<SchoolMbti> optionalSchoolMbti
                = schoolMbtiRepo.findBySchoolAndMbtiAndGraduationYear(account.getSchool(), mbti, Long.valueOf(account.getGraduationYear()));
        if(optionalSchoolMbti.isPresent()){
            optionalSchoolMbti.get().minusOneCount();
        }else{
            schoolMbtiRepo.save(new SchoolMbti(mbti, account.getSchool(),0L));
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
