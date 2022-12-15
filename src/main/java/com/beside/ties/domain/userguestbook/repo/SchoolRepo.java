package com.beside.ties.domain.userguestbook.repo;

import com.beside.ties.domain.school.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepo extends JpaRepository<School, Long> {
}
