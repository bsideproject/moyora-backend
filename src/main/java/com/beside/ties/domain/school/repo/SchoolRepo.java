package com.beside.ties.domain.school.repo;

import com.beside.ties.domain.school.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchoolRepo extends JpaRepository<School, Long> {

    Optional<School> findSchoolBySchoolCode(String schoolCode);
    School findSchoolById(Long id);
    School findBySchoolName(String schoolName);
}
