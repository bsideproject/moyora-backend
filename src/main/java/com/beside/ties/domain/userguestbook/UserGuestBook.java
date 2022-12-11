package com.beside.ties.domain.userguestbook;

import com.beside.ties.common.BaseTimeEntity;
import com.beside.ties.domain.account.Account;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class UserGuestBook extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_guest_book_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    Account account;

    @Column(length = 2000)
    String content;

    @Column(name = "friend_id", unique = true)
    Long friendId;
}
