package com.beside.ties.domain.notebox.service;

import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.account.repo.AccountRepo;
import com.beside.ties.domain.notebox.dto.request.NoteBoxRequest;
import com.beside.ties.domain.notebox.repo.NoteBoxRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class NoteBoxService {

    private final NoteBoxRepo noteBoxRepo;
    private final AccountRepo accountRepo;

/*    public Page<UserGuestBookResponseDto> findUserGuestBooks(Long id, Pageable pageable){
        Account account = accountRepo.findById(id).get();
        Page<UserGuestBookResponseDto> dtos = userGuestBookRepo.findAllByAccount(pageable, account).map(UserGuestBookResponseDto::toDto);
        return dtos;
    }*/

    public void registerGuestBook(NoteBoxRequest request, Account account) {

    }
}
