package com.beside.ties.domain.jobcategory.api;

import com.beside.ties.domain.jobcategory.dto.JobResponseDto;
import com.beside.ties.domain.jobcategory.service.JobCategoryService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "직업 카테고리 API")
@RequiredArgsConstructor
@RequestMapping("api/v1/job/category")
@RestController
public class JobCategoryApi {

    private final JobCategoryService jobCategoryService;


    @Operation(summary = "직업 전체 조회")
    @GetMapping
    ResponseEntity<List<JobResponseDto>> findAlL(){
        return ResponseEntity.ok().body(jobCategoryService.findAll());
    }

    @Operation(summary = "직업 저장")
    @PostMapping
    ResponseEntity<Long> save(
            @RequestParam String jobName
    ){
        return ResponseEntity.ok().body(jobCategoryService.saveByName(jobName));
    }


}
