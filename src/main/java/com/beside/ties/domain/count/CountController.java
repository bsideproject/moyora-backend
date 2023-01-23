package com.beside.ties.domain.count;

import com.beside.ties.domain.account.service.AccountService;
import com.beside.ties.domain.schoolguestbook.repo.SchoolGuestBookRepo;
import com.beside.ties.domain.schoolguestbook.service.SchoolGuestBookService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "총 COUNT API")
@RequestMapping("/api/v1/count")
@RequiredArgsConstructor
@RestController
public class CountController
{
    private final AccountService accountService;
    private final SchoolGuestBookRepo schoolGuestBookRepo;


    @Operation(summary = "총 참여자 수")
    @GetMapping("/user")
    public ResponseEntity<Long> findAllUserCount(){
        return ResponseEntity.ok().body(accountService.getAllUserCount()+1253);
    }

    @Operation(summary = "활성화 된 학교 수")
    @GetMapping("/school")
    public ResponseEntity<Integer> findAllActiveSchool(){
        return ResponseEntity.ok().body(accountService.getActivatedSchool()+113);
    }

    @Operation(summary = "누적 방명록 수")
    @GetMapping("/schoolguestbook")
    public ResponseEntity<Long> findAllSchoolGuestBook(){
        return ResponseEntity.ok().body(schoolGuestBookRepo.count()+156);
    }
}
