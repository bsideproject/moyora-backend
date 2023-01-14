package com.beside.ties.domain.articletouser.api;

import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.articletouser.dto.request.ArticleToUserRegisterRequestDto;
import com.beside.ties.domain.articletouser.dto.request.ArticleToUserUpdateRequestDto;
import com.beside.ties.domain.articletouser.dto.response.ArticleToUserResponseDto;
import com.beside.ties.domain.articletouser.service.ArticleToUserService;
import com.beside.ties.global.common.annotation.CurrentUser;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "쪽지 API")
@RequestMapping("/api/v1/user/article")
@RequiredArgsConstructor
@RestController
public class ArticleToUserApi {

    private final ArticleToUserService articleToUserService;

    @Operation(summary = "쪽지 가져오기")
    @GetMapping
    ResponseEntity<List<ArticleToUserResponseDto>> findArticleByGuestBook(
            @RequestParam(name = "user_id") Long userId
    ){
        return ResponseEntity.ok().body(articleToUserService.findAllByGuestBook(userId));
    }

    @Operation(summary = "나의 쪽지 가져오기")
    @GetMapping("/my")
    ResponseEntity<List<ArticleToUserResponseDto>> findMyArticle(
            @CurrentUser Account account
    ){
        List<ArticleToUserResponseDto> responseDto = articleToUserService.findMyArticle(account);

        return ResponseEntity.ok().body(responseDto);
    }

    @Operation(summary = "유저 쪽지 등록하기")
    @PostMapping
    ResponseEntity<String> registerArticleToUser(
            @RequestBody ArticleToUserRegisterRequestDto requestDto,
            @CurrentUser Account account
    ){
        return ResponseEntity.ok().body(articleToUserService.writeArticle(requestDto, account));
    }

    @Operation(summary = "유저 쪽지 수정하기")
    @PutMapping
    ResponseEntity<String> updateArticle(
            @RequestBody ArticleToUserUpdateRequestDto requestDto,
            @CurrentUser Account account
    ){
        return ResponseEntity.ok().body(articleToUserService.updateArticle(requestDto, account));
    }

    @Operation(summary = "유저 쪽지 삭제하기")
    @DeleteMapping
    void deleteArticle(
            @RequestParam Long articleId,
            @CurrentUser Account account
    ){
        ResponseEntity.ok().body(articleToUserService.deleteArticle(articleId, account));
    }
}
