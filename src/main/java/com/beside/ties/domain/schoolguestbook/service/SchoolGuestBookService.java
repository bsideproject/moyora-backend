package com.beside.ties.domain.schoolguestbook.service;

import com.beside.ties.domain.schoolguestbook.dto.SchoolGuestBookUpdateDto;
import com.beside.ties.domain.schoolguestbook.entity.SchoolGuestBook;
import com.beside.ties.domain.schoolguestbook.repo.SchoolGuestBookRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        return schoolGuestBookRepo.findBySchool_IdOrderByCreatedDateDesc(id);
    }

    public List<SchoolGuestBook> findByAccountId(Long accountId) {
        return schoolGuestBookRepo.findByAccount_IdOrderByCreatedDateDesc(accountId);
    }

    public SchoolGuestBook findById(Long id) {
        return schoolGuestBookRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("SchoolGuestBook doesn't exist"));
    }

    public void contentUpdate(SchoolGuestBookUpdateDto schoolGuestBookUpdateDto) {
        SchoolGuestBook schoolGuestBook = findById(schoolGuestBookUpdateDto.getSchoolGuestBookId());
        schoolGuestBook.contentUpdate(schoolGuestBookUpdateDto.getContent());
    }

    public void delete(Long id) {
        schoolGuestBookRepo.deleteById(id);
    }

    public void deleteAllInBatch() {
        schoolGuestBookRepo.deleteAllInBatch();
    }

}
