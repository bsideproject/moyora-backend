package com.beside.ties.domain.notebox.entity;

import com.beside.ties.domain.note.entity.Note;
import com.beside.ties.global.common.BaseTimeEntity;
import com.beside.ties.domain.account.entity.Account;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class NoteBox extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_box_id")
    Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", unique = true)
    Account account;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "noteBox")
    List<Note> noteList;

    public NoteBox(Account account){
        this.account = account;
    }


}
