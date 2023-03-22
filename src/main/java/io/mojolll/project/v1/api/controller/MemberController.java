package io.mojolll.project.v1.api.controller;

import io.mojolll.project.v1.api.dto.request.MemberLoginRequest;
import io.mojolll.project.v1.api.dto.request.MemberJoinRequest;
import io.mojolll.project.v1.api.dto.request.TestRequest;
import io.mojolll.project.v1.api.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity join(@RequestBody MemberJoinRequest dto){
        memberService.join(dto.getName(),dto.getPassword());
        return ResponseEntity.ok().body("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody MemberLoginRequest dto){
        String token = memberService.login(dto.getName(), dto.getPassword());
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/test")
    public ResponseEntity test(@RequestBody TestRequest dto, Authentication authentication){
        List<Object> list = List.of(dto, authentication.getName());
        return ResponseEntity.ok().body(list.get(1) + "님의 test");
    }
}
