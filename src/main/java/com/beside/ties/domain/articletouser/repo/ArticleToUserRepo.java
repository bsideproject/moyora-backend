package com.beside.ties.domain.articletouser.repo;

import com.beside.ties.domain.articletouser.entity.ArticleToUser;
import com.beside.ties.domain.userguestbook.entity.UserGuestBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleToUserRepo extends JpaRepository<ArticleToUser, Long> {

    List<ArticleToUser> findAllByUserGuestBook(UserGuestBook userGuestBook);

}
