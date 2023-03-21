package io.mojolll.project.v1.api.service;

import io.mojolll.project.v1.api.entity.Member;
import io.mojolll.project.v1.api.exception.AppCustomException;
import io.mojolll.project.v1.api.exception.ErrorCode;
import io.mojolll.project.v1.api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public String join (String name, String password) {

        //Member id 중복
        memberRepository.findByName(name)
                .ifPresent(user -> {
                    throw new AppCustomException(ErrorCode.USERNAME_DUPLICATED, name + "는 이미 가입된 이름입니다.");
                });

//        저장
        Member member = Member.builder()
                .name(name)
                .password(password)
                .build();
        memberRepository.save(member);
        return "success";
    }

}
