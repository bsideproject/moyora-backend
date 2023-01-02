package com.beside.ties.domain.articletouser.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ArticleToUserUpdateRequestDto {
    Long articleId;
    String content;
}
