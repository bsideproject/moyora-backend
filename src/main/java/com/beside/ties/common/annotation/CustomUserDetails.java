package com.beside.ties.common.annotation;

import com.beside.ties.domain.account.Account;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class CustomUserDetails extends User {

    private Account account;

    public CustomUserDetails(Account account) {
        super(account.getPhoneKey(), account.getPw(), List.of(new SimpleGrantedAuthority("ROLE_"+account.getRole())));
        this.account = account;
    }

}
