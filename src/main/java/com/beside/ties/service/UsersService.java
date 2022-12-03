package com.beside.ties.service;

import com.beside.ties.auth.kakao.KakaoUser;
import com.beside.ties.domain.users.Users;
import com.beside.ties.domain.users.UsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UsersService {

    @Autowired
    private final UsersRepo usersRepo;

    public void findByEmail(String email){
        Users users = usersRepo.findUsersByEmail(email).get();

    }

    public Long register(KakaoUser kakaoUser){
        Users user = Users.toUserFromKakao(kakaoUser);
        Users save = usersRepo.save(user);

        if(save == null) throw new IllegalArgumentException("유저 저장 실패");

        return save.getId();

    }
}
