package io.mojolll.project.v1.api.controller;

import io.mojolll.project.v1.api.entity.dto.request.MemberJoinRequest;
import io.mojolll.project.v1.api.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody MemberJoinRequest dto){
        memberService.join(dto.getName(),dto.getPassword());
        return ResponseEntity.ok().body("회원가입 성공");
    }
}
