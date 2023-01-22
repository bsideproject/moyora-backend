package com.beside.ties.domain.notebox.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;


@Data
@Builder
@Getter
@AllArgsConstructor
public class NoteBoxResponse {
    private Long id;
    private String content;
    private LocalDateTime modifiedDate;
/*
    public static UserGuestBookResponseDto toDto(UserGuestBook userGuestBook){
        return UserGuestBookResponseDto.builder()
                .content(userGuestBook.getContent())
                .modifiedDate(userGuestBook.getModifiedDate())
                .id(userGuestBook.getId())
                .build();
    }*/
}
