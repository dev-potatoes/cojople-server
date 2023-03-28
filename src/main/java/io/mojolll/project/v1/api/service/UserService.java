package io.mojolll.project.v1.api.service;

import io.mojolll.project.v1.api.config.jwt.TokenUtils;
import io.mojolll.project.v1.api.dto.LoginRequestDto;
import io.mojolll.project.v1.api.dto.SignUpRequestDto;
import io.mojolll.project.v1.api.model.User;
import io.mojolll.project.v1.api.model.UserRole;
import io.mojolll.project.v1.api.repositroy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

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

    //token 있는거
    public User login(String token) {
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