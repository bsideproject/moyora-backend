package com.beside.ties.domain.schoolguestbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchoolGuestBookAddDto {
    private Long schoolId;
    private String content;
    private String sticker;
}
