package com.beside.ties.domain.jobcategory.dto.response;

import com.beside.ties.domain.jobcategory.entity.JobCategory;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class JobResponseDto {
    public String name;

    public JobResponseDto(JobCategory jobCategory) {
        this.name = jobCategory.getName();
    }

    public static JobResponseDto toDto(JobCategory jobCategory) {
        return new JobResponseDto(jobCategory.getName());
    }
}
