package com.beside.ties.domain.userguestbook;

import com.beside.ties.domain.userguestbook.service.UserGuestBookService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "유저 게스트 북")
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/guestbook")
@RestController
public class UserGuestBookApi {

    private final UserGuestBookService userGuestBookService;
}
