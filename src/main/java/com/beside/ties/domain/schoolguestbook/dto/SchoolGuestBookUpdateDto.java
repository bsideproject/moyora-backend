package com.beside.ties.domain.schoolguestbook.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchoolGuestBookUpdateDto {
    @JsonProperty("SchoolGuestBook_Id")
    private Long schoolGuestBookId;
    private String content;
}
