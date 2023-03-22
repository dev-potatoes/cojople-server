package io.mojolll.project.v1.api.service;

import io.mojolll.project.v1.api.entity.Member;
import io.mojolll.project.v1.api.exception.AppCustomException;
import io.mojolll.project.v1.api.exception.ErrorCode;
import io.mojolll.project.v1.api.repository.MemberRepository;
import io.mojolll.project.v1.api.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @Value("${jwt.secret}")
    private String secretKey;
    private Long expireTimeMs = 1000 * 60 * 60L; //1시간 설정
    public String join (String name, String password) {

        //Member id 중복
        memberRepository.findByName(name)
                .ifPresent(user -> {
                    throw new AppCustomException(ErrorCode.USERNAME_DUPLICATED, name + "는 이미 가입된 이름입니다.");
                });

//        저장
        Member member = Member.builder()
                .name(name)
                .password(passwordEncoder.encode(password))
                .build();
        memberRepository.save(member);
        return "success";
    }

    public String login(String name, String password) {
        //userName 없음
        Member selectMember = memberRepository.findByName(name)
                .orElseThrow(() -> new AppCustomException(ErrorCode.USERNAME_NOT_FOUND, name + "이 없습니다."));
        //Password 틀림
        log.info("selectedPw:{} pw:{}",selectMember.getPassword(),password);
        if (!passwordEncoder.matches(password,selectMember.getPassword())){
            throw new AppCustomException(ErrorCode.INVALID_PASSWORD,"비밀번호가 일치하지 않습니다.");
        }

        //예외발생X -> token발행
        return JwtUtil.createToken(selectMember.getName(), secretKey, expireTimeMs);
    }
}
