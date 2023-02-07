package com.beside.ties.domain.schoolguestbook.service;

import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.account.entity.QAccount;
import com.beside.ties.domain.school.repo.SchoolRepo;
import com.beside.ties.domain.schoolguestbook.dto.SchoolGuestBookUpdateDto;
import com.beside.ties.domain.schoolguestbook.entity.QSchoolGuestBook;
import com.beside.ties.domain.schoolguestbook.entity.SchoolGuestBook;
import com.beside.ties.domain.schoolguestbook.repo.SchoolGuestBookRepo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.beside.ties.domain.account.entity.QAccount.account;

@RequiredArgsConstructor
@Transactional
@Service
public class SchoolGuestBookService {

    private final SchoolGuestBookRepo schoolGuestBookRepo;
    private final SchoolRepo schoolRepo;
    private final EntityManager em;

    public Long save(SchoolGuestBook schoolGuestBook){
        if (schoolGuestBook.getSticker() == null || schoolGuestBook.getSticker().equals("")) {
            schoolGuestBook.settingRandomSticker();
        }
        SchoolGuestBook save = schoolGuestBookRepo.save(schoolGuestBook);
        return save.getId();
    }

    public List<SchoolGuestBook> findByAccount(Account me) {

        JPAQueryFactory query = new JPAQueryFactory(em);
        QSchoolGuestBook schoolGuestBook = QSchoolGuestBook.schoolGuestBook;
        return query
                .selectFrom(schoolGuestBook)
                .leftJoin(schoolGuestBook.account, account)
                .where(
                        account.graduationYear.eq(me.getGraduationYear()),
                        schoolGuestBook.school.eq(me.getSchool())
                )
                .orderBy(schoolGuestBook.createdDate.desc())
                .fetch();
        //return schoolGuestBookRepo.findAllBySchoolAndGraduationYearOrderByCreatedDate(me.getSchool(), me.getGraduationYear());
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

    public int countAllBySchool(Account me) {
        if(me.getSchool() == null) return 0;
        JPAQueryFactory query = new JPAQueryFactory(em);
        QSchoolGuestBook schoolGuestBook = QSchoolGuestBook.schoolGuestBook;
        return query
                .selectFrom(schoolGuestBook)
                .leftJoin(schoolGuestBook.account, account)
                .where(
                        account.graduationYear.eq(me.getGraduationYear()),
                        schoolGuestBook.school.eq(me.getSchool())
                )
                .fetch().size();
        //return schoolGuestBookRepo.countAllBySchoolAndGraduationYear(me.getSchool(), me.getGraduationYear());
    }
}
