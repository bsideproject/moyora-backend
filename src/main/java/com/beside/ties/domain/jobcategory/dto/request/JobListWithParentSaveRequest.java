package com.beside.ties.domain.jobcategory.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class JobListWithParentSaveRequest {

    private String parentName;
    private List<String> jobNames;
}
