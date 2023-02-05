package com.beside.ties.domain.schooljob.service;

import com.beside.ties.domain.common.dto.StatisticsDto;
import com.beside.ties.domain.jobcategory.entity.JobCategory;
import com.beside.ties.domain.jobcategory.repo.JobCategoryRepo;
import com.beside.ties.domain.region.entity.Region;
import com.beside.ties.domain.school.entity.School;
import com.beside.ties.domain.schooljob.entity.SchoolJob;
import com.beside.ties.domain.schooljob.repo.SchoolJobRepo;
import com.beside.ties.domain.schoolregion.entity.SchoolRegion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class SchoolJobService {

    private final SchoolJobRepo schoolJobRepo;
    private final JobCategoryRepo jobCategoryRepo;

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
        JobCategory jobCategory = jobCategoryRepo.findById(schoolJob.getJobCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("Job doesn't exist"));

        SchoolJob resultSchoolJob = SchoolJob.createSchoolJob(jobCategory.getParent(), schoolJob.getSchool(), schoolJob.getCount());
        SchoolJob saveSchoolJob = schoolJobRepo.save(resultSchoolJob);
        return saveSchoolJob.getId();
    }

    public SchoolJob findBySchoolIdAndJobId(Long schoolId, Long jobId) {
        return schoolJobRepo.findBySchool_IdAndJobCategory_Id(schoolId, jobId)
                .orElseThrow(() -> new IllegalArgumentException("SchoolJob doesn't exist"));
    }

    public void countPlusOneUpdate(SchoolJob schoolJob) {
        schoolJob.plusOneCount();
    }

    public List<StatisticsDto> convertStatisticsDto(List<SchoolJob> schoolJobs, Long totalCount) {
        return schoolJobs.stream()
                .map(item ->
                        new StatisticsDto(
                                item.getJobCategory().getName(),
                                percentCalculate(item.getCount(), totalCount)))
                .collect(Collectors.toList());
    }

    public void countPlus(School school, JobCategory jobCategory) {
        Optional<SchoolJob> optionalSchoolJob = schoolJobRepo.findBySchool_IdAndJobCategory_Id(school.getId(), jobCategory.getId());
        if(optionalSchoolJob.isPresent()){
            optionalSchoolJob.get().plusOneCount();
        }else{
            if(jobCategory.getParent() != null) {
                schoolJobRepo.save(new SchoolJob(jobCategory.getParent(), school));
            }
        }
    }

    public List<Long> convertTop5Percent(List<SchoolJob> schoolRegions, Long totalCount) {
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
        schoolJobRepo.deleteAllInBatch();
    }
}
