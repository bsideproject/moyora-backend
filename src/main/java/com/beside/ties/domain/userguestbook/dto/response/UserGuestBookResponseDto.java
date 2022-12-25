package com.beside.ties.domain.userguestbook.dto.response;

import com.beside.ties.domain.userguestbook.entity.UserGuestBook;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;


@Data
@Builder
@Getter
@AllArgsConstructor
public class UserGuestBookResponseDto {
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
