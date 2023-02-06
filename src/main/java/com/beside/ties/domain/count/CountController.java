package com.beside.ties.domain.count;

import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.account.service.AccountService;
import com.beside.ties.domain.schoolguestbook.repo.SchoolGuestBookRepo;
import com.beside.ties.domain.schoolguestbook.service.SchoolGuestBookService;
import com.beside.ties.global.common.annotation.CurrentUser;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "총 COUNT API")
@RequestMapping("/api/v1/count")
@RequiredArgsConstructor
@RestController
public class CountController
{
    private final AccountService accountService;
    private final SchoolGuestBookRepo schoolGuestBookRepo;
    private final SchoolGuestBookService schoolGuestBookService;


    @Operation(summary = "총 참여자 수")
    @GetMapping("/user")
    public ResponseEntity<Long> findAllUserCount(){
        return ResponseEntity.ok().body(accountService.getAllUserCount()+246);
    }

    @Operation(summary = "활성화 된 학교 수")
    @GetMapping("/school")
    public ResponseEntity<Integer> findAllActiveSchool(){
        return ResponseEntity.ok().body(accountService.getActivatedSchool()+64);
    }

    @Operation(summary = "누적 방명록 수")
    @GetMapping("/schoolguestbook")
    public ResponseEntity<Long> findAllSchoolGuestBook(){
        return ResponseEntity.ok().body(schoolGuestBookRepo.count()+448);
    }

    @Operation(summary = "나의 학교 가입한 동창")
    @GetMapping("/schoolmate")
    public ResponseEntity<Long> findAllSchoolMateCount(
            @RequestParam Long schoolId,
            @CurrentUser Account account
    ){
        return ResponseEntity.ok().body(accountService.getAllSchoolMateCount(account));
    }

    @Operation(summary = "나의 학교 방명록 수")
    @GetMapping("/our/schoolguestbook")
    public ResponseEntity<Integer> findAllOurSchoolGuestBook(
            @RequestParam Long schoolId,
            @CurrentUser Account account
    ){
        return ResponseEntity.ok().body(schoolGuestBookService.countAllBySchool(account));
    }
}
