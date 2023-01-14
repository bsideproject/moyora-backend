package com.beside.ties.domain.schoolguestbook.entity;

import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.school.entity.School;
import com.beside.ties.domain.schoolguestbook.dto.SchoolGuestBookDto;
import com.beside.ties.global.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SchoolGuestBook extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "sgb_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    private String content;

    public SchoolGuestBook(School school, Account account, String content) {
        this.school = school;
        this.account = account;
        this.content = content;
    }

    public void contentUpdate(String content) {
        this.content = content;
    }

    public static SchoolGuestBookDto toSchoolGuestBookDto(SchoolGuestBook schoolGuestBook) {
        return new SchoolGuestBookDto(
                schoolGuestBook.getId(),
                schoolGuestBook.getSchool().getId(),
                schoolGuestBook.getAccount().getId(),
                schoolGuestBook.getContent(),
                schoolGuestBook.getCreatedDate(),
                schoolGuestBook.getModifiedDate()
        );
    }
}
