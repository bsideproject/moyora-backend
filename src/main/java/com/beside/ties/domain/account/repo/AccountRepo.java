package com.beside.ties.domain.account.repo;

import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.school.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepo extends JpaRepository<Account, Long> {

    Optional<Account> findAccountByEmail(String email);

    Optional<Account> findAccountByKakaoId(String kakaoId);

    List<Account> findAllBySchool(School school);

    List<Account> findAllBySchoolAndNameStartsWith(School school, String name);


    Long countAllBySchool(School school);
}
