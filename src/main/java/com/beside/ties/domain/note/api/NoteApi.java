package com.beside.ties.domain.note.api;

import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.note.dto.request.RegisterNoteRequest;
import com.beside.ties.domain.note.dto.request.NoteUpdateRequest;
import com.beside.ties.domain.note.dto.response.NoteResponse;
import com.beside.ties.domain.note.service.NoteService;
import com.beside.ties.global.common.annotation.CurrentUser;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "쪽지 API")
@RequestMapping("/api/v1/user/note")
@RequiredArgsConstructor
@RestController
public class NoteApi {

    private final NoteService noteService;

    @Operation(summary = "쪽지 가져오기")
    @GetMapping
    ResponseEntity<List<NoteResponse>> findNotes(
            @RequestParam(name = "user_id") Long userId
    ){
        return ResponseEntity.ok().body(noteService.findAllNote(userId));
    }

    @Operation(summary = "나의 쪽지 가져오기")
    @GetMapping("/my")
    ResponseEntity<List<NoteResponse>> findMyNote(
            @CurrentUser Account account
    ){
        List<NoteResponse> responseDto = noteService.findMyNote(account);

        return ResponseEntity.ok().body(responseDto);
    }

    @Operation(summary = "유저 쪽지 등록하기")
    @PostMapping
    ResponseEntity<String> registerNote(
            @RequestBody RegisterNoteRequest requestDto,
            @CurrentUser Account account
    ){
        return ResponseEntity.ok().body(noteService.writeNote(requestDto, account));
    }

    @Operation(summary = "유저 쪽지 수정하기")
    @PutMapping
    ResponseEntity<String> updateNote(
            @RequestBody NoteUpdateRequest requestDto,
            @CurrentUser Account account
    ){
        return ResponseEntity.ok().body(noteService.updateNote(requestDto, account));
    }

    @Operation(summary = "유저 쪽지 삭제하기")
    @DeleteMapping
    void deleteNote(
            @RequestParam Long noteId,
            @CurrentUser Account account
    ){
        ResponseEntity.ok().body(noteService.deleteNote(noteId, account));
    }
}
