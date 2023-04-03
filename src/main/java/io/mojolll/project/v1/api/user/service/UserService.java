package io.mojolll.project.v1.api.user.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.mojolll.project.v1.api.config.jwt.JwtProperties;
import io.mojolll.project.v1.api.config.jwt.TokenUtils;
import io.mojolll.project.v1.api.redis.logout.LogoutAccessTokenFromRedis;
import io.mojolll.project.v1.api.redis.logout.LogoutAccessTokenRedisRepository;
import io.mojolll.project.v1.api.redis.refresh.RefreshTokenFromRedis;
import io.mojolll.project.v1.api.redis.refresh.RefreshTokenRedisRepository;
import io.mojolll.project.v1.api.user.dto.UserRequestDto;
import io.mojolll.project.v1.api.user.model.User;
import io.mojolll.project.v1.api.user.model.UserRole;
import io.mojolll.project.v1.api.user.repositroy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;


    public User signUp(final UserRequestDto userDto) {
//        logoutAccessTokenRedisRepository.deleteAll();
//        refreshTokenRedisRepository.deleteAll();
        final User user = User.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .roles(Collections.singletonList(UserRole.ROLE_USER.name()))
                .build();

        return userRepository.save(user);
    }

    public User login(final UserRequestDto userDto) {
        User searchUser = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("아이디가 일치하지 않습니다."));

        if (!passwordEncoder.matches(userDto.getPassword(),searchUser.getPassword())){
            throw new RuntimeException();
        }

        String refreshToken = TokenUtils.generateJwtRefreshToken(searchUser);
        RefreshTokenFromRedis refreshTokenFromRedis = RefreshTokenFromRedis.
                createRefreshToken(refreshToken, searchUser.getEmail(),
                        TokenUtils.getExpireTimeFromRefreshToken(refreshToken).getTime());

        refreshTokenRedisRepository.save(refreshTokenFromRedis);
        return searchUser;
    }

    public ResponseEntity<LogoutAccessTokenFromRedis> logout(String token) {
        String userEmailFromAccessToken = TokenUtils.getUserEmailFromAccessToken(token);
        Date expireTimeFromAccessToken = TokenUtils.getExpireTimeFromAccessToken(token);
        LogoutAccessTokenFromRedis logoutAccessToken = LogoutAccessTokenFromRedis.createLogoutAccessToken(token, userEmailFromAccessToken,
                expireTimeFromAccessToken.getTime());

        logoutAccessTokenRedisRepository.save(logoutAccessToken);
        return ResponseEntity.ok().body(logoutAccessToken);
    }

    //token 있는거
    public ResponseEntity<User> login2(String token) {

        String userEmailFromToken = TokenUtils.getUserEmailFromAccessToken(token);

        if(logoutAccessTokenRedisRepository.findById(token).isEmpty()){
            return ResponseEntity.ok().body(userRepository.findByEmail(userEmailFromToken)
                    .orElseThrow(() -> new UsernameNotFoundException(token + "에 해당하는 유저를 찾을 수 없습니다.")));
        }

        return ResponseEntity.badRequest().build();
    }

    public boolean isEmailDuplicated(final String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<RefreshTokenFromRedis> findAllRefreshToken() {
        List<RefreshTokenFromRedis> list = new ArrayList<>();
        Iterable<RefreshTokenFromRedis> all = refreshTokenRedisRepository.findAll();
        for (RefreshTokenFromRedis refreshTokenFromRedis : all) {
            list.add(refreshTokenFromRedis);
        }
        return list;
    }

    @Transactional(readOnly = true)
    public List<LogoutAccessTokenFromRedis> findAllLogoutToken() {
        List<LogoutAccessTokenFromRedis> list = new ArrayList<>();
        Iterable<LogoutAccessTokenFromRedis> all = logoutAccessTokenRedisRepository.findAll();
        for (LogoutAccessTokenFromRedis logoutAccessTokenFromRedis : all) {
            list.add(logoutAccessTokenFromRedis);
        }
        return list;
    }
}