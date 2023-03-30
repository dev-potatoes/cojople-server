package io.mojolll.project.v1.api.user.service;

import io.mojolll.project.v1.api.config.jwt.TokenUtils;
import io.mojolll.project.v1.api.user.dto.LoginRequestDto;
import io.mojolll.project.v1.api.user.dto.SignUpRequestDto;
import io.mojolll.project.v1.api.user.model.User;
import io.mojolll.project.v1.api.user.model.UserRole;
import io.mojolll.project.v1.api.user.repositroy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public User signUp(final SignUpRequestDto signUpDTO) {
        final User user = User.builder()
                .email(signUpDTO.getEmail())
                .password(passwordEncoder.encode(signUpDTO.getPassword()))
                .roles(Collections.singletonList(UserRole.ROLE_USER.name()))
                .build();

        return userRepository.save(user);
    }

    public User login(final LoginRequestDto loginDto) {
        User searchUser = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("아이디가 일치하지 않습니다."));

        if (!passwordEncoder.matches(loginDto.getPassword(),searchUser.getPassword())){
            throw new RuntimeException();
        }
        return searchUser;
    }

    //token 있는거
    public User login2(String token) {
        String userEmailFromToken = TokenUtils.getUserEmailFromToken(token);

        return userRepository.findByEmail(userEmailFromToken)
                .orElseThrow(() -> new UsernameNotFoundException(userEmailFromToken));
    }

    public boolean isEmailDuplicated(final String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

}