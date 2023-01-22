package com.beside.ties.domain.notebox.api;

import com.beside.ties.domain.notebox.service.NoteBoxService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "쪽지함 API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/notebox")
@RestController
public class NoteBoxApi {

    private final NoteBoxService noteBoxService;

    // 나의 게스트 북 찾기

    // 친구의 게스트 북 찾기

}
