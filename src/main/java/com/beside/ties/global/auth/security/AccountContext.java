package com.beside.ties.global.auth.security;

import com.beside.ties.domain.account.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class AccountContext extends User {

    private Account account;

    static Collection<? extends GrantedAuthority> parseRole(Account account){
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(account.getRole().getName()));
        return authorities;
    }

    public AccountContext(Account account){
        super(account.getKakaoId(),account.getPw(),parseRole(account));
        this.account = account;
    }
}
