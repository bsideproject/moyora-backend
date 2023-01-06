package com.beside.ties.domain.schoolguestbook.api;

import com.beside.ties.domain.ResponseVo;
import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.school.entity.School;
import com.beside.ties.domain.school.service.SchoolService;
import com.beside.ties.domain.schoolguestbook.dto.SchoolGuestBookAddDto;
import com.beside.ties.domain.schoolguestbook.dto.SchoolGuestBookDto;
import com.beside.ties.domain.schoolguestbook.dto.SchoolGuestBookUpdateDto;
import com.beside.ties.domain.schoolguestbook.entity.SchoolGuestBook;
import com.beside.ties.domain.schoolguestbook.service.SchoolGuestBookService;
import com.beside.ties.global.common.annotation.CurrentUser;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "지역 API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/schoolGuestBook")
@RestController
public class SchoolGuestBookApi {

    private final SchoolGuestBookService schoolGuestBookService;
    private final SchoolService schoolService;

    @Operation(summary = "학교 방명록 조회")
    @GetMapping("/{schoolId}")
    public ResponseEntity<?> findAllSchoolGuestBook(@PathVariable Long schoolId) {
        List<SchoolGuestBook> schoolGuestBookList = schoolGuestBookService.findBySchoolId(schoolId);
        List<SchoolGuestBookDto> schoolGuestBookDtoList = schoolGuestBookList.stream()
                .map(SchoolGuestBook::toSchoolGuestBookDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(
                new ResponseVo(
                        schoolGuestBookDtoList
                ));
    }

    @Operation(summary = "내가 작성한 방명록 조회")
    @GetMapping("/me")
    public ResponseEntity<?> findAllSchoolGuestBookByAccountId(@CurrentUser Account account) {
        List<SchoolGuestBook> schoolGuestBookList = schoolGuestBookService.findByAccountId(account.getId());
        List<SchoolGuestBookDto> schoolGuestBookDtoList = schoolGuestBookList.stream()
                .map(SchoolGuestBook::toSchoolGuestBookDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(
                new ResponseVo(
                        schoolGuestBookDtoList
                ));
    }

    @Operation(summary = "학교 방명록 등록")
    @PostMapping("/")
    public ResponseEntity<?> saveSchoolGuestBook(@CurrentUser Account account,
                                           @RequestBody SchoolGuestBookAddDto schoolGuestBookAddDto) {
        School school = schoolService.findSchoolById(schoolGuestBookAddDto.getSchoolId());

        SchoolGuestBook schoolGuestBook = new SchoolGuestBook(school, account, schoolGuestBookAddDto.getContent());

        schoolGuestBookService.save(schoolGuestBook);

        return ResponseEntity.ok().body(
                new ResponseVo(
                        "등록 성공(미확정)"
                ));
    }

    @Operation(summary = "학교 방명록 수정")
    @PutMapping("/")
    public ResponseEntity<?> updateSchoolGuestBook(@RequestBody SchoolGuestBookUpdateDto schoolGuestBookUpdateDto) {
        schoolGuestBookService.update(schoolGuestBookUpdateDto);
        return ResponseEntity.ok().body(
                new ResponseVo(
                        "업데이트 성공(미확정)"
                ));
    }

    @Operation(summary = "학교 방명록 삭제")
    @DeleteMapping("/{SchoolGuestBookId}")
    public ResponseEntity<?> deleteSchoolGuestBook(@PathVariable Long SchoolGuestBookId) {
        schoolGuestBookService.delete(SchoolGuestBookId);
        return ResponseEntity.ok().body(
                new ResponseVo(
                        "삭제 성공(미확정)"
                ));
    }
}
