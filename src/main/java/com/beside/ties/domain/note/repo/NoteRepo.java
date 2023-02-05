package com.beside.ties.domain.note.repo;

import com.beside.ties.domain.note.entity.Note;
import com.beside.ties.domain.notebox.entity.NoteBox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepo extends JpaRepository<Note, Long> {

    List<Note> findAllByNoteBoxOrderByCreatedDateAsc(NoteBox noteBox);

}
