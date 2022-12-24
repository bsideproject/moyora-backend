package com.beside.ties.domain.userguestbook.service;

import com.beside.ties.domain.account.repo.AccountRepo;
import com.beside.ties.domain.userguestbook.repo.UserGuestBookRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserGuestBookService {

    private final UserGuestBookRepo userGuestBookRepo;
    private final AccountRepo accountRepo;

    void findUserGuestBooks(Long id){
    }
}
