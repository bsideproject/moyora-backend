package com.beside.ties.domain.jobcategory.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class JobListWithParentSaveRequest {

    @JsonProperty("parent_name")
    private String parentName;
    private List<String> jobNames;
}
