package com.beside.ties.domain.school.api;

import com.beside.ties.domain.ResponseVo;
import com.beside.ties.domain.school.service.SchoolService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "학교 API")
@RequestMapping("/api/v1/school")
@RequiredArgsConstructor
@RestController
public class SchoolApi {

    private final SchoolService schoolService;

    @Operation(summary = "전체 학교 조회")
    @GetMapping("/")
    public ResponseEntity<?> findAllSchool() {
        return ResponseEntity.ok().body(
                new ResponseVo(
                        schoolService.findAllSchool()
                ));
    }

    @Operation(summary = "특전 학교 조회")
    @GetMapping("/{schoolId}")
    public ResponseEntity<?> findSchoolById(@PathVariable Long schoolId) {
        return ResponseEntity.ok().body(
                new ResponseVo(
                        schoolService.findSchoolById(schoolId)
                ));
    }
}
