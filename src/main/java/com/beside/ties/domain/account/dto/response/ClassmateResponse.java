package com.beside.ties.domain.account.dto.response;

import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.school.entity.School;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ClassmateResponse {
    private Long id;
    private String nickname;
    private String username;
    private String profile;
    private String schoolName;

    public static ClassmateResponse toDto(Account account){
        return ClassmateResponse.builder()
                .id(account.getId())
                .nickname(account.getNickname())
                .profile(account.getProfile())
                .username(account.getRealName())
                .schoolName(account.getSchool().getSchoolName())
                .build();
    }
}
