package com.beside.ties.domain.articletouser.api;

import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.articletouser.dto.request.ArticleToUserRequestDto;
import com.beside.ties.domain.articletouser.dto.response.ArticleToUserResponseDto;
import com.beside.ties.domain.articletouser.service.ArticleToUserService;
import com.beside.ties.global.common.annotation.CurrentUser;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "유저에게 방명록 API")
@RequestMapping("/api/v1/user/article")
@RequiredArgsConstructor
@RestController
public class ArticleToUserApi {

    private final ArticleToUserService articleToUserService;

    @Operation(summary = "유저 방명록 글 가져오기")
    @GetMapping
    ResponseEntity<List<ArticleToUserResponseDto>> findArticleByGuestBook(
            @RequestParam(name = "user_id") Long userId
    ){
        return ResponseEntity.ok().body(articleToUserService.findAllByGuestBook(userId));
    }

    @Operation(summary = "유저 방명록 글 수정하기")
    @PostMapping
    ResponseEntity<String> updateArticle(
            @RequestBody ArticleToUserRequestDto requestDto,
            @CurrentUser Account account
    ){
        return ResponseEntity.ok().body(articleToUserService.updateArticle(requestDto, account));
    }

    @Operation(summary = "유저 방명록 글 삭제하기")
    @DeleteMapping
    void deleteArticle(
            @RequestParam Long articleId,
            @CurrentUser Account account
    ){
        ResponseEntity.ok().body(articleToUserService.deleteArticle(articleId, account));
    }
}
