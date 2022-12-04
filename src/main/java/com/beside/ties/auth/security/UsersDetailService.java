package com.beside.ties.auth.security;

import com.beside.ties.common.annotation.CustomUserDetails;
import com.beside.ties.domain.users.Users;
import com.beside.ties.domain.users.UsersRepo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service("userDetailsService")
public class UsersDetailService implements UserDetailsService {

    private final UsersRepo usersRepo;
    @Getter
    private Users users;

    @Override
    public UserDetails loadUserByUsername(String phoneKey) throws UsernameNotFoundException {

        users = usersRepo.findUsersByPhoneKey(phoneKey).get();

        return users;

    }
}
