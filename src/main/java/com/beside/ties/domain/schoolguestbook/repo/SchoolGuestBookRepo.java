package com.beside.ties.domain.schoolguestbook.repo;

import com.beside.ties.domain.schoolguestbook.entity.SchoolGuestBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchoolGuestBookRepo extends JpaRepository<SchoolGuestBook, Long> {

    List<SchoolGuestBook> findSchoolGuestBookBySchool_Id(Long schoolId);
}
