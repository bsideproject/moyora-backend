package com.beside.ties.domain.articletouser.entity;

import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.userguestbook.entity.UserGuestBook;
import com.beside.ties.global.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ArticleToUser extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_to_user_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id")
    Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_guest_book_id")
    UserGuestBook userGuestBook;

    @Column(length = 1200)
    String content;

    public ArticleToUser(Account account, UserGuestBook userguestBook, String content){
        this.account = account;
        this.userGuestBook = userguestBook;
        this.content = content;
    }

    public void updateContent(String content){
        this.content = content;
    }

}
