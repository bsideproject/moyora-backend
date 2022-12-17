package com.beside.ties.domain.school.service;

import com.beside.ties.domain.school.entity.School;
import com.beside.ties.domain.userguestbook.repo.SchoolRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class SchoolService {

    private final SchoolRepo schoolRepo;

    public Optional<School> checkSchoolCode(String schoolCode) {
        Optional<School> schoolOptional = schoolRepo.findSchoolBySchoolCode(schoolCode);
        return schoolOptional;

    }

    public Long save(School school){
        School save = schoolRepo.save(school);
        return save.getId();
    }
}
