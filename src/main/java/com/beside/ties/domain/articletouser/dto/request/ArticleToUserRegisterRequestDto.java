package com.beside.ties.domain.articletouser.dto.request;

import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.articletouser.entity.ArticleToUser;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ArticleToUserRegisterRequestDto {
    @JsonProperty("user_guest_book_id")
    private Long UserGuestBookId;
    private String content;

    private Boolean isPublic;

    private String sticker;

    public ArticleToUserRegisterRequestDto(Long UserGuestBookId, String content, Boolean isPublic, String sticker) {
        this.UserGuestBookId = UserGuestBookId;
        this.content = content;
        this.isPublic = isPublic;
        this.sticker = sticker;
    }

    static public ArticleToUser ToDto(ArticleToUserRegisterRequestDto request, Account account){
        return ArticleToUser.builder()
                .content(request.getContent())
                .isPublic(request.getIsPublic())
                .sticker(request.getSticker())
                .content(request.getContent())
                .account(account)
                .build();
    }
}
