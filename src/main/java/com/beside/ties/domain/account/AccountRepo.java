package com.beside.ties.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepo extends JpaRepository<Account, Long> {

    Optional<Account> findAccountByEmail(String email);

    Optional<Account> findAccountByKakaoId(String kakaoId);
}
