package io.mojolll.project.v1.api.service;

import io.mojolll.project.v1.api.entity.UserRole;
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
    @Value("${jwt.token-validity-in-seconds}")
    private Long expireTimeMs; //1시간 설정
    public String join (String email, String password) {

        //Member id 중복
        memberRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new AppCustomException(ErrorCode.USERNAME_DUPLICATED, email + "는 이미 가입된 이름입니다.");
                });

//        저장
        Member member = Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(UserRole.ROLE_USER) //나중에 이메일 인증하면 ROLE_USER 처리하기
                .build();
        memberRepository.save(member);
        return "success";
    }

    public String login(String email, String password) {
        //userName 없음
        Member selectMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new AppCustomException(ErrorCode.USERNAME_NOT_FOUND, email + "이 없습니다."));
        //Password 틀림
        log.info("selectedPw:{} pw:{}",selectMember.getPassword(),password);
        if (!passwordEncoder.matches(password,selectMember.getPassword())){
            throw new AppCustomException(ErrorCode.INVALID_PASSWORD,"비밀번호가 일치하지 않습니다.");
        }

        //예외발생X -> token발행
        return JwtUtil.createToken(selectMember, secretKey, expireTimeMs);
    }
}
