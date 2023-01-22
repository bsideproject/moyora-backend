package com.beside.ties.domain.note.service;

import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.account.repo.AccountRepo;
import com.beside.ties.domain.note.dto.request.RegisterNoteRequest;
import com.beside.ties.domain.note.dto.request.NoteUpdateRequest;
import com.beside.ties.domain.note.dto.response.NoteResponse;
import com.beside.ties.domain.note.entity.Note;
import com.beside.ties.domain.note.repo.NoteRepo;
import com.beside.ties.domain.notebox.entity.NoteBox;
import com.beside.ties.domain.notebox.repo.NoteBoxRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class NoteService {

    private final NoteRepo noteRepo;
    private final AccountRepo accountRepo;
    private final NoteBoxRepo noteBoxRepo;


    @Transactional(readOnly = true)
    public List<NoteResponse> findAllNote(Long userId) {
        Account account = accountRepo.findById(userId).get();
        NoteBox noteBox = noteBoxRepo.findByAccount(account).get();
        return noteRepo.findAllByNoteBox(noteBox)
                .stream().map(NoteResponse::toDto).collect(Collectors.toList());
    }


    public String writeNote(RegisterNoteRequest requestDto, Account account){

        Optional<Account> optionalAccount = accountRepo.findById(requestDto.getUserId());
        if(optionalAccount.isEmpty()){
            throw new IllegalStateException("존재하지 않는 계정입니다.");
        }
        Account other = optionalAccount.get();

        Optional<NoteBox> noteBoxOptional = noteBoxRepo.findByAccount(other);

        if(noteBoxOptional.isEmpty()){
            throw new IllegalStateException("해당 아이디의 쪽지함이 존재하지 않습니다.");
        }

        Note save = noteRepo.save(RegisterNoteRequest.toEntity(requestDto,account,noteBoxOptional.get()));

        return "ID : "+save.getId()+" 방명록 저장이 완료되었습니다.";
    }


    public String deleteNote(Long noteId, Account account){
        noteRepo.delete(getNote(noteId, account));
        return "삭제가 완료되었습니다.";
    }


    public String updateNote(NoteUpdateRequest requestDto, Account account){
        Note note = getNote(requestDto.getNoteId(), account);
        note.updateContent(requestDto.getContent());
        return "업데이트가 완료되었습니다.";
    }

    private Note getNote(Long noteId, Account account) {
        Optional<Note> optionalNote = noteRepo.findById(noteId);
        if (optionalNote.isEmpty()) {
            throw new IllegalStateException("해당 방명록이 존재하지 않습니다.");
        }
        Note note = optionalNote.get();
        if (note.getAccount().getId() != account.getId()) {
            throw new IllegalStateException("본인이 작성한 글이 아니라서 권한이 없습니다.");
        }
        return optionalNote.get();
    }


    public List<NoteResponse> findMyNote(Account account) {
        NoteBox noteBox = noteBoxRepo.findByAccount(account).get();
        return noteRepo.findAllByNoteBox(noteBox)
                .stream().map(NoteResponse::toMyDto).collect(Collectors.toList());
    }
}
