package com.beside.ties.domain.articletouser.api;

import com.beside.ties.domain.BaseMvcTest;
import com.beside.ties.domain.articletouser.dto.request.ArticleToUserRegisterRequestDto;
import com.beside.ties.domain.articletouser.repo.ArticleToUserRepo;
import com.beside.ties.domain.userguestbook.entity.UserGuestBook;
import com.beside.ties.domain.userguestbook.repo.UserGuestBookRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class ArticleToUserApiTest extends BaseMvcTest {

    @Autowired
    UserGuestBookRepo userGuestBookRepo;

    @Autowired
    ArticleToUserRepo articleToUserRepo;

    static Long UserGuestBookId;


    @DisplayName("/api/v1/user/article")
    @Test
    void findArticleByGuestBook() {


    }

    @WithUserDetails("test@naver.com")
    @DisplayName("유저 방명록에 글 등록하기 테스트")
    @Test
    void RegisterArticleByGuestBook() throws Exception {


        ArticleToUserRegisterRequestDto requestDto
        = new ArticleToUserRegisterRequestDto(UserGuestBookId, "content",true,"1");

        mockMvc.perform(
                post("/api/v1/user/article")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("방명록 저장이 완료되었습니다.")))
                .andDo(print());



    }

    @Test
    void updateArticle() {

    }

    @Test
    void deleteArticle() {
    }


    @BeforeEach
    void beforeEach(){
        Optional<UserGuestBook> optionalUserGuestBook = userGuestBookRepo.findUserGuestBookByAccount(testMember);
        if(optionalUserGuestBook.isPresent()){
            userGuestBookRepo.delete(optionalUserGuestBook.get());
        }

        UserGuestBook save = userGuestBookRepo.save(new UserGuestBook(testMember));
        UserGuestBookId = save.getId();


    }

    @AfterEach
    void afterEach(){

    }
}