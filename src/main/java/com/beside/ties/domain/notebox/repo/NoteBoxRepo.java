package com.beside.ties.domain.notebox.repo;

import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.notebox.entity.NoteBox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoteBoxRepo extends JpaRepository<NoteBox, Long> {


    Optional<NoteBox> findByAccount(Account account);
}
