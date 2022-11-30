package com.beside.ties.domain.users;

import com.beside.ties.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "USERS_SEQ_GEN",
        sequenceName = "USERS_SEQ",
        initialValue = 10000,
        allocationSize = 50
)
@Getter
@Entity
public class Users extends BaseTimeEntity {

    @Builder
    public Users(
            String email,
            String password,
            String nickname,
            String username,
            String phoneNum,
            String phoneKey
    )
    {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phoneNum = phoneNum;
        this.phoneKey = phoneKey;
        this.username = username;
        this.profile = "default";
    }

    @Id
    @GeneratedValue(generator = "USERS_SEQ_GEN")
    @Column(name = "users_id")
    Long id;

    @Column(nullable = false, unique = true, length = 50)
    String email;

    @Column(nullable = false, length = 25)
    String password;

    @Column(nullable = false, length = 50)
    String nickname;

    @Column(nullable = false, unique = true,name = "phone_num",length = 11)
    String phoneNum;

    @Column(nullable = false, unique = true,name = "phone_key")
    String phoneKey;

    @Column(nullable = false, length = 25)
    String username;

    @Column(nullable = false)
    String profile;

    void updatePassword(String password) {
        this.password = password;
    }
}
