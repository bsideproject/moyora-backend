package com.beside.ties.domain.account.repo;

import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.school.entity.School;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepo extends JpaRepository<Account, Long> {

    Optional<Account> findAccountByEmail(String email);

    Optional<Account> findAccountByKakaoId(String kakaoId);


    List<Account> findAllBySchoolAndKakaoIdNotAndGraduationYear(School school, String kakaoId, int graduationYear);


    //findAllBySchool
    List<Account> findAllBySchoolAndNameContainsAndGraduationYear(School school, String name, int graduationYear);

    @Override
    @EntityGraph(attributePaths = {"myJob", "mbti", "region"})
    Optional<Account> findById(Long aLong);

    Long countAllBySchool(School school);
    Long countAllBySchoolAndGraduationYear(School school, int graduationYear);
}
