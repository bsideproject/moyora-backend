package com.beside.ties.domain.mbti.repo;

import com.beside.ties.domain.mbti.entity.Mbti;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MbtiRepo extends JpaRepository<Mbti, Long> {

    Optional<Mbti> findByName(String name);
}
