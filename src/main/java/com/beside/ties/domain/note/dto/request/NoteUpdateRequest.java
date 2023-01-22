package com.beside.ties.domain.note.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NoteUpdateRequest {
    Long noteId;
    String content;
}
