package com.beside.ties.auth.security;

import com.beside.ties.domain.users.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class UsersContext extends User {

    private Users users;

    static Collection<? extends GrantedAuthority> parseRole(Users users){
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(users.getRole().getName()));
        return authorities;
    }

    public UsersContext(Users users){
        super(users.getPhoneKey(),users.getPassword(),parseRole(users));
        this.users = users;
    }
}
