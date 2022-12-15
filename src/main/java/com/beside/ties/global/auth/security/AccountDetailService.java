package com.beside.ties.global.auth.security;

import com.beside.ties.global.common.annotation.CustomUserDetails;
import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.account.repo.AccountRepo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service("userDetailsService")
public class AccountDetailService implements UserDetailsService {

    private final AccountRepo accountRepo;
    @Getter
    private Account account;

    @Override
    public UserDetails loadUserByUsername(String kakaoId) throws UsernameNotFoundException {

        account = accountRepo.findAccountByKakaoId(kakaoId).get();

        return new CustomUserDetails(account);

    }
}
