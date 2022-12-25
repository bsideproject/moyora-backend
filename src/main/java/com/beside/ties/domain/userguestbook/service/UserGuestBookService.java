package com.beside.ties.domain.userguestbook.service;

import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.account.repo.AccountRepo;
import com.beside.ties.domain.userguestbook.dto.request.UserGuestBookRequestDto;
import com.beside.ties.domain.userguestbook.dto.response.UserGuestBookResponseDto;
import com.beside.ties.domain.userguestbook.entity.UserGuestBook;
import com.beside.ties.domain.userguestbook.repo.UserGuestBookRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserGuestBookService {

    private final UserGuestBookRepo userGuestBookRepo;
    private final AccountRepo accountRepo;

/*    public Page<UserGuestBookResponseDto> findUserGuestBooks(Long id, Pageable pageable){
        Account account = accountRepo.findById(id).get();
        Page<UserGuestBookResponseDto> dtos = userGuestBookRepo.findAllByAccount(pageable, account).map(UserGuestBookResponseDto::toDto);
        return dtos;
    }*/

    public void registerGuestBook(UserGuestBookRequestDto request, Account account) {

    }
}
