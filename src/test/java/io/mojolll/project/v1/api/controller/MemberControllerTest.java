//package io.mojolll.project.v1.api.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.mojolll.project.v1.api.dto.request.MemberLoginRequest;
//import io.mojolll.project.v1.api.dto.request.MemberJoinRequest;
//import io.mojolll.project.v1.api.exception.AppCustomException;
//import io.mojolll.project.v1.api.exception.ErrorCode;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
//import org.springframework.test.web.servlet.MockMvc;
//
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest
//public class MemberControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @MockBean
//    MemberService memberService;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//
//    @Test
//    @DisplayName("회원가입 성공")
//    @WithMockUser
//    void join_success() throws Exception {
//        String name = "name1";
//        String password = "password";
//
//        mockMvc.perform(post("/api/v1/users/join")
//                        .with(SecurityMockMvcRequestPostProcessors.csrf()) //spring security dependency
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(new MemberJoinRequest(
//                                password,
//                                name
//                        ))))  //http method에 값보낼때는 byte로 보내기때문 (stream)
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("회원가입 실페 - memberName 중복")
//    @WithMockUser
//    void join_fail() throws Exception {
//        String name = "name1";
//        String password = "password";
//
//        //name, password 아무거나 넘긴다. 넘기는 값 상관없이 throw 되게
//        //원래 join() return type void 였는데 컴파일에러 발생해서 String으로 바꿈
//        Mockito.when(memberService.join(Mockito.any(),Mockito.any()))
//                        .thenThrow(new AppCustomException(ErrorCode.USERNAME_DUPLICATED, name + "는 이미 가입된 이름입니다.")); //메세지 ExceptionManager에서 처리
//
//        mockMvc.perform(post("/api/v1/users/join")
//                        .with(SecurityMockMvcRequestPostProcessors.csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(new MemberJoinRequest(
//                                password,
//                                name
//                        ))))  //http method에 값보낼때는 byte로 보내기때문 (stream)
//                .andDo(print())
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @DisplayName("로그인 성공")
//    @WithMockUser
//    void login_success() throws Exception {
//
//        String name = "name1";
//        String password = "password";
//
//        Mockito.when(memberService.login(Mockito.any(),Mockito.any()))
//                        .thenReturn("token");
//
//        mockMvc.perform(post("/api/v1/users/login")
//                        .with(SecurityMockMvcRequestPostProcessors.csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(new MemberLoginRequest(
//                                password,
//                                name
//                        ))))  //http method에 값보낼때는 byte로 보내기때문 (stream)
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("로그인 실패 - userName 없음")
//    @WithMockUser
////spring security를 통과할 때 @WithAnonymousUser로 처리할 때는
//// .with(SecurityMockMvcRequestPostProcessors.csrf()) 뒷부분이 실행이 안되서 @WithMockUser로 바꿔
//    void login_fail1() throws Exception {
//
//        String name = "name1";
//        String password = "password";
//
//        Mockito.when(memberService.login(Mockito.any(),Mockito.any()))
//                .thenThrow(new AppCustomException(ErrorCode.USERNAME_NOT_FOUND,""));
//
//        mockMvc.perform(post("/api/v1/users/login")
//                        .with(SecurityMockMvcRequestPostProcessors.csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(new MemberLoginRequest(
//                                password,
//                                name
//                        ))))  //http method에 값보낼때는 byte로 보내기때문 (stream)
//                .andDo(print())
//                .andExpect(status().isNotFound());
//    }
//
//
//    @Test
//    @DisplayName("로그인 실패 - password 틀림")
//    @WithMockUser
//    void login_fail2() throws Exception {
//        String name = "name1";
//        String password = "password";
//
//        Mockito.when(memberService.login(Mockito.any(),Mockito.any()))
//                .thenThrow(new AppCustomException(ErrorCode.INVALID_PASSWORD,""));
//
//        mockMvc.perform(post("/api/v1/users/login")
//                        .with(SecurityMockMvcRequestPostProcessors.csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(new MemberLoginRequest(
//                                password,
//                                name
//                        ))))  //http method에 값보낼때는 byte로 보내기때문 (stream)
//                .andDo(print())
//                .andExpect(status().isUnauthorized());
//    }
//}
