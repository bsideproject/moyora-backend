package com.beside.ties.domain.articletouser.dto.response;


import com.beside.ties.domain.articletouser.entity.ArticleToUser;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class ArticleToUserResponseDto {

    private long articleId;
    private long friendId;
    private String content;

    public static ArticleToUserResponseDto toDto(ArticleToUser articleToUser){
        return ArticleToUserResponseDto.builder()
                .articleId(articleToUser.getId())
                .friendId(articleToUser.getAccount().getId())
                .content(articleToUser.getContent()).build();
    }
}
