package com.beside.ties.domain.userguestbook.entity;

import com.beside.ties.domain.articletouser.entity.ArticleToUser;
import com.beside.ties.global.common.BaseTimeEntity;
import com.beside.ties.domain.account.entity.Account;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class UserGuestBook extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_guest_book_id")
    Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", unique = true)
    Account account;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userGuestBook")
    List<ArticleToUser> articlesToUser;

    public UserGuestBook(Account account){
        this.account = account;
    }


}
