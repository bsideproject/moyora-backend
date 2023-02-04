package com.beside.ties.domain.schoolguestbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SchoolGuestBookDto {
    private Long SchoolGuestBookId;
    private Long schoolId;
    private Long accountId;
    private String content;
    private String sticker;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
