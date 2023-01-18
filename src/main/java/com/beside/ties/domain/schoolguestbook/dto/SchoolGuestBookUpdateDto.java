package com.beside.ties.domain.schoolguestbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchoolGuestBookUpdateDto {
    private Long schoolGuestBookId;
    private String content;
}
