package com.beside.ties.domain.articletouser.dto.response;


import com.beside.ties.domain.articletouser.entity.ArticleToUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;


@ApiModel
@Builder
@Getter
public class ArticleToUserResponseDto {

    @ApiModelProperty(
            name = "아티클 ID",
            example = "10023"
    )
    private long articleId;

    @ApiModelProperty(
            name = "아티클 ID",
            example = "10023"
    )
    private long friendId;

    @ApiModelProperty(
            name = "공개 여부 설정",
            example = "true"
    )
    private Boolean isPublic;

    @ApiModelProperty(
            name = "내용",
            example = "내용"
    )
    private String content;

    @ApiModelProperty(
            name = "스티커",
            example = "1"
    )
    private String sticker;

    @ApiModelProperty(
            name = "닉네임",
            example = "알랑까몰라"
    )
    private String nickname;

    @ApiModelProperty(
            name = "유저 이름",
            example = "김종완"
    )
    private String username;

    public static ArticleToUserResponseDto toDto(ArticleToUser articleToUser){

        if(articleToUser.getIsPublic()) {
            return ArticleToUserResponseDto.builder()
                    .articleId(articleToUser.getId())
                    .friendId(articleToUser.getAccount().getId())
                    .sticker(articleToUser.getSticker())
                    .isPublic(articleToUser.getIsPublic())
                    .content(articleToUser.getContent())
                    .nickname(articleToUser.getAccount().getNickname())
                    .username(articleToUser.getAccount().getUsername())
                    .build();
        }
        else{
            return ArticleToUserResponseDto.builder()
                    .articleId(articleToUser.getId())
                    .friendId(articleToUser.getAccount().getId())
                    .sticker(articleToUser.getSticker())
                    .isPublic(articleToUser.getIsPublic())
                    .nickname(articleToUser.getAccount().getNickname())
                    .username(articleToUser.getAccount().getUsername())
                    .content("비공개")
                    .build();
        }
    }

    public static ArticleToUserResponseDto toMyDto(ArticleToUser articleToUser){

            return ArticleToUserResponseDto.builder()
                    .articleId(articleToUser.getId())
                    .friendId(articleToUser.getAccount().getId())
                    .sticker(articleToUser.getSticker())
                    .isPublic(articleToUser.getIsPublic())
                    .content(articleToUser.getContent())
                    .nickname(articleToUser.getAccount().getNickname())
                    .username(articleToUser.getAccount().getUsername())
                    .build();
    }

}
