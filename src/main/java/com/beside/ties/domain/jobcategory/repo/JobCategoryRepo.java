package com.beside.ties.domain.jobcategory.repo;

import com.beside.ties.domain.jobcategory.entity.JobCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobCategoryRepo extends JpaRepository<JobCategory, Long> {
    Optional<JobCategory> findJobCategoryByName(String name);


}
