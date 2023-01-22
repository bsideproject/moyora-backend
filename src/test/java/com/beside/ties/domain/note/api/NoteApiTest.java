package com.beside.ties.domain.note.api;

import com.beside.ties.domain.BaseMvcTest;
import com.beside.ties.domain.note.dto.request.RegisterNoteRequest;
import com.beside.ties.domain.note.repo.NoteRepo;
import com.beside.ties.domain.notebox.entity.NoteBox;
import com.beside.ties.domain.notebox.repo.NoteBoxRepo;
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
class NoteApiTest extends BaseMvcTest {

    @Autowired
    NoteBoxRepo noteBoxRepo;

    @Autowired
    NoteRepo noteRepo;

    static Long noteBoxId;


    @DisplayName("/api/v1/user/note")
    @Test
    void findNote() {


    }

    @WithUserDetails("test@naver.com")
    @DisplayName("유저 쪽지함에 글 등록하기 테스트")
    @Test
    void RegisterNote() throws Exception {


        RegisterNoteRequest requestDto
        = new RegisterNoteRequest(noteBoxId, "content",true,"1");

        mockMvc.perform(
                post("/api/v1/user/note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("방명록 저장이 완료되었습니다.")))
                .andDo(print());



    }

    @Test
    void updateNote() {

    }

    @Test
    void deleteNote() {
    }


    @BeforeEach
    void beforeEach(){
        Optional<NoteBox> optionalNoteBox = noteBoxRepo.findByAccount(testMember);
        if(optionalNoteBox.isPresent()){
            noteBoxRepo.delete(optionalNoteBox.get());
        }

        NoteBox save = noteBoxRepo.save(new NoteBox(testMember));
        noteBoxId = save.getId();


    }

    @AfterEach
    void afterEach(){

    }
}