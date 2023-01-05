package com.beside.ties.domain.schoolguestbook.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchoolGuestBookAddDto {
    @JsonProperty("school_id")
    private Long schoolId;
    private String content;
}
