package com.beside.ties.domain.schoolguestbook.repo;

import com.beside.ties.domain.schoolguestbook.entity.SchoolGuestBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SchoolGuestBookRepo extends JpaRepository<SchoolGuestBook, Long> {

    List<SchoolGuestBook> findBySchool_IdOrderByCreatedDateDesc(Long schoolId);
    List<SchoolGuestBook> findByAccount_IdOrderByCreatedDateDesc(Long accountId);
    Optional<SchoolGuestBook> findById(Long schoolGuestBookId);
}
