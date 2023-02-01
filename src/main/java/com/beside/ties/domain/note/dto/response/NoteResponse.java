package com.beside.ties.domain.note.dto.response;


import com.beside.ties.domain.note.entity.Note;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;


@ApiModel
@Builder
@Getter
public class NoteResponse {

    @ApiModelProperty(
            name = "쪽지 ID",
            example = "10023"
    )
    private long noteId;

    @ApiModelProperty(
            name = "작성자 이름",
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

    @ApiModelProperty(
            name = "생성날짜",
            example = "2022/07/07"
    )
    private String createdDate;

    public static NoteResponse toDto(Note note){

        if(note.getIsPublic()) {
            return NoteResponse.builder()
                    .noteId(note.getId())
                    .friendId(note.getAccount().getId())
                    .sticker(note.getSticker())
                    .isPublic(note.getIsPublic())
                    .content(note.getContent())
                    .nickname(note.getAccount().getNickname())
                    .username(note.getAccount().getUsername())
                    .createdDate(note.getCreatedDate().toLocalDate().toString().replace("-","/"))
                    .build();
        }
        else{
            return NoteResponse.builder()
                    .noteId(note.getId())
                    .friendId(note.getAccount().getId())
                    .sticker(note.getSticker())
                    .isPublic(note.getIsPublic())
                    .nickname(note.getAccount().getNickname())
                    .username(note.getAccount().getUsername())
                    .createdDate(note.getCreatedDate().toLocalDate().toString().replace("-","/"))
                    .content("비공개")
                    .build();
        }
    }

    public static NoteResponse toMyDto(Note note){

            return NoteResponse.builder()
                    .noteId(note.getId())
                    .friendId(note.getAccount().getId())
                    .sticker(note.getSticker())
                    .isPublic(note.getIsPublic())
                    .content(note.getContent())
                    .nickname(note.getAccount().getNickname())
                    .username(note.getAccount().getUsername())
                    .createdDate(note.getCreatedDate().toLocalDate().toString().replace("-","/"))
                    .build();
    }

}
