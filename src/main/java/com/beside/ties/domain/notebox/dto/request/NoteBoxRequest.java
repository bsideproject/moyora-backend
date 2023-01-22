package com.beside.ties.domain.notebox.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;


@Data
@Builder
@Getter
@AllArgsConstructor
public class NoteBoxRequest {
    private Long id;
    private String content;
    private LocalDateTime modifiedDate;
/*
    public static UserGuestBookRequestDto toDto(UserGuestBook userGuestBook){
        return UserGuestBookRequestDto.builder()
                .content(userGuestBook.getContent())
                .modifiedDate(userGuestBook.getModifiedDate())
                .id(userGuestBook.getId())
                .build();
    }*/
}
