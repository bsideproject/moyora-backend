package com.beside.ties.domain.school.api;

import com.beside.ties.domain.ResponseVo;
import com.beside.ties.domain.school.dto.SchoolDto;
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

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "학교 API")
@RequestMapping("/api/v1/school")
@RequiredArgsConstructor
@RestController
public class SchoolApi {

    private final SchoolService schoolService;

    @Operation(summary = "전체 학교 조회")
    @GetMapping("/")
    public ResponseEntity<?> findAllSchool() {
        List<SchoolDto> schoolDtoList = schoolService.findAllSchool().stream()
                .map(SchoolDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(
                new ResponseVo(
                        schoolDtoList
                ));
    }

    @Operation(summary = "특전 학교 조회")
    @GetMapping("/{schoolId}")
    public ResponseEntity<?> findSchoolById(@PathVariable Long schoolId) {
        SchoolDto schoolDto = new SchoolDto(schoolService.findSchoolById(schoolId));
        return ResponseEntity.ok().body(
                new ResponseVo(
                        schoolDto
                ));
    }
}
