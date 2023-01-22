package com.beside.ties.domain.note.dto.request;

import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.note.entity.Note;
import com.beside.ties.domain.notebox.entity.NoteBox;
import lombok.Getter;

@Getter
public class RegisterNoteRequest {
    private Long userId;
    private String content;

    private Boolean isPublic;

    private String sticker;

    public RegisterNoteRequest(Long userId, String content, Boolean isPublic, String sticker) {
        this.userId = userId;
        this.content = content;
        this.isPublic = isPublic;
        this.sticker = sticker;
    }

    static public Note toEntity(RegisterNoteRequest request, Account account, NoteBox noteBox){
        return Note.builder()
                .content(request.getContent())
                .isPublic(request.getIsPublic())
                .noteBox(noteBox)
                .sticker(request.getSticker())
                .content(request.getContent())
                .account(account)
                .build();
    }
}
