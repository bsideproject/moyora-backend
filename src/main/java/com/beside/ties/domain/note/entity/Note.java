package com.beside.ties.domain.note.entity;

import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.notebox.entity.NoteBox;
import com.beside.ties.global.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Note extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id")
    Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_box_id")
    NoteBox noteBox;

    @Column(length = 1200)
    String content;

    @Column(name = "is_public")
    Boolean isPublic;

    @Column(length = 10)
    String sticker;

    @Builder
    public Note(Account account, NoteBox noteBox, String content, Boolean isPublic, String sticker){
        this.account = account;
        this.noteBox = noteBox;
        this.content = content;
        this.isPublic = isPublic;
        this.sticker = sticker;
    }

    public void updateContent(String content){
        this.content = content;
    }

}
