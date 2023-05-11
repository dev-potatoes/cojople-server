package io.mojolll.project.v1.api.user.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.mojolll.project.v1.api.config.jwt.TokenUtils;
import io.mojolll.project.v1.api.exception.TokenNotFoundException;
import io.mojolll.project.v1.api.exception.RefreshTokenExpireException;
import io.mojolll.project.v1.api.redis.logout.LogoutAccessTokenFromRedis;
import io.mojolll.project.v1.api.redis.logout.LogoutAccessTokenRedisRepository;
import io.mojolll.project.v1.api.redis.refresh.RefreshTokenFromRedis;
import io.mojolll.project.v1.api.redis.refresh.RefreshTokenRedisRepository;
import io.mojolll.project.v1.api.user.dto.ReissueDto;
import io.mojolll.project.v1.api.user.dto.UserRequestDto;
import io.mojolll.project.v1.api.user.model.User;
import io.mojolll.project.v1.api.user.model.UserRole;
import io.mojolll.project.v1.api.user.repositroy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;


    public User signUp(final UserRequestDto userDto) {
        final User user = User.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .roles(Collections.singletonList(UserRole.ROLE_USER.name()))
                .build();

        return userRepository.save(user);
    }

    public HashMap<String, Object> login(final UserRequestDto userDto) {
        HashMap<String, Object> response = new HashMap<>();

        User searchUser = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(userDto.getEmail()+": 해당 이메일이 존재하지 않습니다."));

        if (!passwordEncoder.matches(userDto.getPassword(),searchUser.getPassword())){
            throw new BadCredentialsException("패스워드가 일치하지 않습니다.");
        }

        Optional<RefreshTokenFromRedis> refreshTokenEntity = refreshTokenRedisRepository.findByEmail(userDto.getEmail());
        Optional<LogoutAccessTokenFromRedis> logoutEntity = logoutAccessTokenRedisRepository.findByEmail(userDto.getEmail());

        if (logoutEntity.isPresent())
            logoutAccessTokenRedisRepository.delete(logoutEntity.get());
        if(refreshTokenEntity.isPresent())
            refreshTokenRedisRepository.delete(refreshTokenEntity.get());

        String accessToken = TokenUtils.generateJwtAccessToken(searchUser);

        String refreshToken = TokenUtils.generateJwtRefreshToken(searchUser);
        RefreshTokenFromRedis refreshTokenFromRedis = RefreshTokenFromRedis.
                createRefreshToken(refreshToken, searchUser.getEmail(), accessToken,
                        TokenUtils.getExpireTimeFromRefreshToken(refreshToken).getTime());

        refreshTokenRedisRepository.save(refreshTokenFromRedis);
        response.put("user",searchUser);
        response.put("accessToken",accessToken);

        return response;
    }

    public ResponseEntity<LogoutAccessTokenFromRedis> logout(String token) {
        String userEmailFromAccessToken = TokenUtils.getUserEmailFromAccessToken(token);
        Date expireTimeFromAccessToken = TokenUtils.getExpireTimeFromAccessToken(token);

        LogoutAccessTokenFromRedis logoutAccessToken = LogoutAccessTokenFromRedis.createLogoutAccessToken(token,
                userEmailFromAccessToken, expireTimeFromAccessToken.getTime());
        logoutAccessTokenRedisRepository.save(logoutAccessToken);


        //refresh token 만료되어 있을 수 있다.
        RefreshTokenFromRedis refreshToken = refreshTokenRedisRepository.findByEmail(userEmailFromAccessToken)
                .orElseThrow(() -> new UsernameNotFoundException(token + "에 해당하는 유저를 찾을 수 없습니다."));

        refreshTokenRedisRepository.delete(refreshToken);
        return ResponseEntity.ok().body(logoutAccessToken);
    }

    //token 있는거
    public ResponseEntity<User> login2(String token) {

        String userEmailFromToken = TokenUtils.getUserEmailFromAccessToken(token);

        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.findByEmail(userEmailFromToken)
                .orElseThrow(() -> new UsernameNotFoundException(token + "에 해당하는 유저를 찾을 수 없습니다.")));
    }

    public String reissue(final ReissueDto reissueDto){

        RefreshTokenFromRedis refreshToken = refreshTokenRedisRepository.findByAccessToken(reissueDto.getAccessToken())
                .orElseThrow(() -> new TokenNotFoundException("해당하는 refreshToken이 없습니다."));

        try {
            TokenUtils.isValidRefreshToken(refreshToken.getId());
        } catch (ExpiredJwtException e) {
            throw new RefreshTokenExpireException("Refresh Token 유효시간 만료");
        } catch (Exception e){
            throw new JwtException("Refresh Token이 유효하지 않습니다.");
        }


        User user = userRepository.findByEmail(refreshToken.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저가 존재하지 않습니다."));

        String accessToken = TokenUtils.generateJwtAccessToken(user);
        refreshToken.updateAccessToken(accessToken);
        refreshTokenRedisRepository.save(refreshToken);

        return accessToken;
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