package com.beside.ties.domain.schoolguestbook.service;

import com.beside.ties.domain.account.dto.request.LocalSignUpRequest;
import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.account.service.AccountService;
import com.beside.ties.domain.school.entity.School;
import com.beside.ties.domain.school.service.SchoolService;
import com.beside.ties.domain.schoolguestbook.dto.SchoolGuestBookUpdateDto;
import com.beside.ties.domain.schoolguestbook.entity.SchoolGuestBook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("학교 방명록 서비스 로직 테스트")
@ActiveProfiles("test")
@SpringBootTest
class SchoolGuestBookServiceTest {

    private static final String phoneNum = "10188888888";
    private static final String email = "GODRI321C@daum.com";
    private static final String name = "가드릭";
    private static final String nickname = "godric";
    private static final String picture = "https://www.balladang.com";

    @Autowired
    private SchoolGuestBookService schoolGuestBookService;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private AccountService accountService;

    private School searchSchool;

    private SchoolGuestBook searchSchoolGuestBook;

    @BeforeEach
    public void beforeEach() {
        LocalSignUpRequest request = new LocalSignUpRequest(
                email,
                picture,
                nickname,
                name,
                phoneNum
        );

        accountService.localSignUp(request);

        Account account = accountService.loadUserByUsername(email);

        School school = new School("테스트학교", "2022-10-11", "테스트주소", "테스트코드");
        School school1 = new School("테스트학교1", "2022-10-12", "테스트주소1", "테스트코드1");
        School school2 = new School("테스트학교2", "2022-10-13", "테스트주소2", "테스트코드2");
        schoolService.save(school);
        schoolService.save(school1);
        schoolService.save(school2);

        searchSchool = schoolService.findBySchoolName("테스트학교").get(0);

        SchoolGuestBook schoolGuestBook1 = new SchoolGuestBook(searchSchool, account, "우리학교 짱1");
        SchoolGuestBook schoolGuestBook2 = new SchoolGuestBook(searchSchool, account, "우리학교 짱2");
        SchoolGuestBook schoolGuestBook3 = new SchoolGuestBook(searchSchool, account, "우리학교 짱3");

        schoolGuestBookService.save(schoolGuestBook1);
        schoolGuestBookService.save(schoolGuestBook2);
        schoolGuestBookService.save(schoolGuestBook3);

        searchSchoolGuestBook = schoolGuestBookService.findBySchoolId(searchSchool.getId()).get(0);
    }

    @AfterEach
    public void afterEach() {
        schoolGuestBookService.deleteAllInBatch();
        schoolService.deleteAllInBatch();
        accountService.deleteAllInBatch();
    }

    @Test
    void findBySchoolIdTest() {
        Account account = accountService.loadUserByUsername(email);
        assertThat(account.getEmail()).isEqualTo(email);

        List<SchoolGuestBook> schoolGuestBookList = schoolGuestBookService.findBySchoolId(searchSchool.getId());
        assertThat(schoolGuestBookList.size()).isEqualTo(3);
    }

    @Test
    void findByAccountIdTest() {
        Account account = accountService.loadUserByUsername(email);

        List<SchoolGuestBook> schoolGuestBookList = schoolGuestBookService.findByAccountId(account.getId());
        assertThat(schoolGuestBookList.size()).isEqualTo(3);
    }

    @Test
    void saveTest() {
        Account account = accountService.loadUserByUsername(email);
        String content = "우리학교 짱4";

        School school = schoolService.findSchoolById(1L);

        SchoolGuestBook schoolGuestBook = SchoolGuestBook.builder()
                .school(school)
                .account(account)
                .content(content)
                .build();

        schoolGuestBookService.save(schoolGuestBook);

        List<SchoolGuestBook> schoolGuestBookList = schoolGuestBookService.findBySchoolId(searchSchool.getId());

        for (SchoolGuestBook guestBook : schoolGuestBookList) {
            System.out.println("guestBook = " + guestBook.getSticker());
        }

        assertThat(schoolGuestBookList.size()).isEqualTo(4);
    }

    @Test
    void updateTest() {
        String content = "우리학교 짱10";
        SchoolGuestBookUpdateDto guestBookUpdateDto = new SchoolGuestBookUpdateDto(searchSchoolGuestBook.getId(), content);

        schoolGuestBookService.contentUpdate(guestBookUpdateDto);

        SchoolGuestBook schoolGuestBook = schoolGuestBookService.findById(searchSchoolGuestBook.getId());
        assertThat(schoolGuestBook.getContent()).isEqualTo(content);

    }

    @Test
    void deleteTest() {
        schoolGuestBookService.delete(searchSchoolGuestBook.getId());

        List<SchoolGuestBook> schoolGuestBookList = schoolGuestBookService.findBySchoolId(searchSchool.getId());
        assertThat(schoolGuestBookList.size()).isEqualTo(2);
    }
}