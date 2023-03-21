package io.mojolll.project.v1.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mojolll.project.v1.api.entity.dto.request.MemberJoinRequest;
import io.mojolll.project.v1.api.exception.AppCustomException;
import io.mojolll.project.v1.api.exception.ErrorCode;
import io.mojolll.project.v1.api.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MemberService memberService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 성공")
    void joinFail() throws Exception {
        String name = "name1";
        String password = "password";

        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new MemberJoinRequest(
                                password,
                                name
                        ))))  //http method에 값보낼때는 byte로 보내기때문 (stream)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 실페 - memberName 중복")
    void join() throws Exception {
        String name = "name1";
        String password = "password";

        //name, password 아무거나 넘긴다. 넘기는 값 상관없이 throw 되게
        //원래 join return type void였는데 컴파일에러 발생해서 String으로 바꿈
        Mockito.when(memberService.join(Mockito.any(),Mockito.any()))
                        .thenThrow(new AppCustomException(ErrorCode.USERNAME_DUPLICATED, name + "는 이미 가입된 이름입니다.")); //메세지 ExceptionManager에서 처리

        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new MemberJoinRequest(
                                password,
                                name
                        ))))  //http method에 값보낼때는 byte로 보내기때문 (stream)
                .andDo(print())
                .andExpect(status().isConflict());
    }
}
