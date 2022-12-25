package com.beside.ties.domain.articletouser.service;

import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.account.repo.AccountRepo;
import com.beside.ties.domain.articletouser.dto.request.ArticleToUserRequestDto;
import com.beside.ties.domain.articletouser.dto.response.ArticleToUserResponseDto;
import com.beside.ties.domain.articletouser.entity.ArticleToUser;
import com.beside.ties.domain.articletouser.repo.ArticleToUserRepo;
import com.beside.ties.domain.userguestbook.entity.UserGuestBook;
import com.beside.ties.domain.userguestbook.repo.UserGuestBookRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleToUserService {

    private final ArticleToUserRepo articleToUserRepo;
    private final AccountRepo accountRepo;
    private final UserGuestBookRepo userGuestBookRepo;


    @Transactional(readOnly = true)
    public List<ArticleToUserResponseDto> findAllByGuestBook(Long userId) {
        Account account = accountRepo.findById(userId).get();
        UserGuestBook userGuestBook = userGuestBookRepo.findUserGuestBookByAccount(account).get();
        return articleToUserRepo.findAllByUserGuestBook(userGuestBook)
                .stream().map(ArticleToUserResponseDto::toDto).collect(Collectors.toList());
    }


    // 방명록 남기기
    public String writeArticle(String content, Account account, Long guestBookId){
        Optional<UserGuestBook> guestBookOptional = userGuestBookRepo.findById(guestBookId);
        if(guestBookOptional.isEmpty()){
            throw new IllegalStateException("해당 아이디의 게스트북이 존재하지 않습니다.");
        }

        ArticleToUser save = articleToUserRepo.save(new ArticleToUser(account, guestBookOptional.get(), content));

        return "ID : "+save.getId()+"방명록 저장이 완료되었습니다.";
    }


    // 방명록 삭제하기
    public String deleteArticle(Long articleId, Account account){
        articleToUserRepo.delete(getArticleToUser(articleId, account));
        return "삭제가 완료되었습니다.";
    }


    // 방명록 수정하기
    public String updateArticle(ArticleToUserRequestDto requestDto, Account account){
        ArticleToUser articleToUser = getArticleToUser(requestDto.getArticleId(), account);
        articleToUser.updateContent(requestDto.getContent());
        return "업데이트가 완료되었습니다.";
    }

    private ArticleToUser getArticleToUser(Long articleId, Account account) {
        Optional<ArticleToUser> optionalArticle = articleToUserRepo.findById(articleId);
        if (optionalArticle.isEmpty()) {
            throw new IllegalStateException("해당 방명록이 존재하지 않습니다.");
        }
        ArticleToUser articleToUser = optionalArticle.get();
        if (articleToUser.getAccount().getId() != account.getId()) {
            throw new IllegalStateException("본인이 작성한 글이 아니라서 권한이 없습니다.");
        }
        return optionalArticle.get();
    }


}
