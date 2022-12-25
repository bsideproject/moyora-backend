package com.beside.ties.domain.articletouser.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ArticleToUserRegisterRequestDto {
    @JsonProperty("user_guest_book_id")
    Long UserGuestBookId;
    String content;

    public ArticleToUserRegisterRequestDto(Long UserGuestBookId, String content) {
        this.UserGuestBookId = UserGuestBookId;
        this.content = content;
    }
}
