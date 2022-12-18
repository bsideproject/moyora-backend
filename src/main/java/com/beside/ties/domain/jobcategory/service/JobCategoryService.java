package com.beside.ties.domain.jobcategory.service;

import com.beside.ties.domain.jobcategory.entity.JobCategory;
import com.beside.ties.domain.jobcategory.repo.JobCategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
}
