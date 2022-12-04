package com.beside.ties.common.annotation;

import com.beside.ties.domain.users.Role;
import com.beside.ties.domain.users.Users;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class CustomUserDetails extends User {

    private Users users;

    public CustomUserDetails(Users users) {
        super(users.getPhoneKey(), users.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_"+users.getRole().getName())));
    }

}
