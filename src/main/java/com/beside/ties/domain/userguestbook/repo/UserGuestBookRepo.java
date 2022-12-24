package com.beside.ties.domain.userguestbook.repo;

import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.userguestbook.entity.UserGuestBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserGuestBookRepo extends JpaRepository<UserGuestBook, Long> {

    List<UserGuestBook> findUserGuestBooksByAccount(Account account);
}
