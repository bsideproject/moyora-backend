package com.beside.ties.domain.jobcategory.dto;

import com.beside.ties.domain.jobcategory.entity.JobCategory;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class JobResponseDto {
    public String name;

    public JobResponseDto(JobCategory jobCategory) {

    }
}
