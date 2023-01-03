package com.beside.ties.domain.schoolguestbook.service;

import com.beside.ties.domain.school.entity.School;
import com.beside.ties.domain.schoolguestbook.entity.SchoolGuestBook;
import com.beside.ties.domain.schoolguestbook.repo.SchoolGuestBookRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class SchoolGuestBookService {

    private final SchoolGuestBookRepo schoolGuestBookRepo;

    public Long save(SchoolGuestBook schoolGuestBook){
        SchoolGuestBook save = schoolGuestBookRepo.save(schoolGuestBook);
        return save.getId();
    }

    public List<SchoolGuestBook> findBySchoolId(Long id) {
        return schoolGuestBookRepo.findSchoolGuestBookBySchool_Id(id);
    }
}
