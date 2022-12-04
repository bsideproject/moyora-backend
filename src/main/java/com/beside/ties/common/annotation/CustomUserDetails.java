package com.beside.ties.common.annotation;

import com.beside.ties.domain.users.Users;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class CustomUserDetails extends User {

    private Users users;

    public CustomUserDetails(Users users) {
        super(users.getPhoneKey(), users.getPw(), List.of(new SimpleGrantedAuthority("ROLE_"+users.getRole())));
        this.users = users;
    }

}
