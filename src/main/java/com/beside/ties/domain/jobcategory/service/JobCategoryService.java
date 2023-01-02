package com.beside.ties.domain.jobcategory.service;

import com.beside.ties.domain.jobcategory.dto.request.JobListSaveRequest;
import com.beside.ties.domain.jobcategory.dto.request.JobListWithParentSaveRequest;
import com.beside.ties.domain.jobcategory.dto.response.JobResponseDto;
import com.beside.ties.domain.jobcategory.entity.JobCategory;
import com.beside.ties.domain.jobcategory.repo.JobCategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class JobCategoryService {

    private final JobCategoryRepo jobCategoryRepo;

    public JobCategory findJobCategoryByName(String name){
        Optional<JobCategory> optionalJobCategory = jobCategoryRepo.findJobCategoryByName(name);

        if(optionalJobCategory.isEmpty()){
            throw new IllegalArgumentException("존재하지 않는 직업입니다.");
        }

        return optionalJobCategory.get();
    }

    public Long save(JobCategory jobCategory){
        JobCategory save = jobCategoryRepo.save(jobCategory);
        return save.getId();
    }

    public String saveByName(JobListSaveRequest request){
        for(int i=0; i<request.getJobNames().size(); i++){
            jobCategoryRepo.save(new JobCategory(request.getJobNames().get(i)));
        }
        return "직업 종류 등록이 완료되었습니다.";
    }

    public String saveByNameAndParent(JobListWithParentSaveRequest request){
        Optional<JobCategory> optionalParent = jobCategoryRepo.findJobCategoryByName(request.getParentName());
        if(optionalParent.isEmpty()){
            throw new IllegalArgumentException("존재하지 않는 직업 종류입니다.");
        }

        for(int i=0; i<request.getJobNames().size(); i++){
            jobCategoryRepo.save(new JobCategory(request.getJobNames().get(i), optionalParent.get()));
        }
        return "직업 등록이 완료되었습니다.";
    }


    public List<JobResponseDto> findAllParent() {
        return jobCategoryRepo.findAllByParentIsNull().stream().map(JobResponseDto::toDto).collect(Collectors.toList());
    }

    public List<JobResponseDto> findAllChild(String category) {
        Optional<JobCategory> categoryOptional = jobCategoryRepo.findJobCategoryByName(category);
        if(categoryOptional.isEmpty()) throw new IllegalArgumentException("존재하지 않는 직업 종류입니다.");
        return jobCategoryRepo.findAllByParent(categoryOptional.get()).stream().map(JobResponseDto::toDto).collect(Collectors.toList());
    }
}
