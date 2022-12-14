package com.beside.ties.domain.userguestbook.repo;

import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.userguestbook.entity.UserGuestBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserGuestBookRepo extends JpaRepository<UserGuestBook, Long> {


    Optional<UserGuestBook> findUserGuestBookByAccount(Account account);
}
