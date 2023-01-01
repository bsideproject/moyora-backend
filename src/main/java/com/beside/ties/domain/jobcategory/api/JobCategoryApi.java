package com.beside.ties.domain.jobcategory.api;

import com.beside.ties.domain.jobcategory.dto.request.JobListSaveRequest;
import com.beside.ties.domain.jobcategory.dto.request.JobListWithParentSaveRequest;
import com.beside.ties.domain.jobcategory.dto.response.JobResponseDto;
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


    @Operation(summary = "직업 종류 전체 조회")
    @GetMapping("/parent")
    ResponseEntity<List<JobResponseDto>> findAlLParent(){
        return ResponseEntity.ok().body(jobCategoryService.findAllParent());
    }

    @Operation(summary = "직업 전체 조회")
    @GetMapping("/child")
    ResponseEntity<List<JobResponseDto>> findAlLChild(
            @RequestParam String category
    ){
        return ResponseEntity.ok().body(jobCategoryService.findAllChild(category));
    }

    @Operation(summary = "직업 종류 여러개 등록하기")
    @PostMapping("/parent")
    ResponseEntity<String> saveParent(
            @RequestParam JobListSaveRequest request
            ){
        return ResponseEntity.ok().body(jobCategoryService.saveByName(request));
    }

    @Operation(summary = "직업 등록 여러개 등록하기")
    @PostMapping("/child")
    ResponseEntity<String> save(
            @RequestBody JobListWithParentSaveRequest request
            ){
        return ResponseEntity.ok().body(jobCategoryService.saveByNameAndParent(request));
    }


}
