package com.beside.ties.domain.userguestbook.api;

import com.beside.ties.domain.account.entity.Account;
import com.beside.ties.domain.userguestbook.dto.request.UserGuestBookRequestDto;
import com.beside.ties.domain.userguestbook.dto.response.UserGuestBookResponseDto;
import com.beside.ties.domain.userguestbook.service.UserGuestBookService;
import com.beside.ties.global.common.annotation.CurrentUser;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "유저 게스트 북")
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/guestbook")
@RestController
public class UserGuestBookApi {

    private final UserGuestBookService userGuestBookService;

    // 나의 게스트 북 찾기

    // 친구의 게스트 북 찾기

}
