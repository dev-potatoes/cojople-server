package io.mojolll.project.v1.api.user.controller;

import io.mojolll.project.v1.api.config.jwt.JwtProperties;
import io.mojolll.project.v1.api.config.jwt.TokenUtils;
import io.mojolll.project.v1.api.redis.logout.LogoutAccessTokenFromRedis;
import io.mojolll.project.v1.api.redis.refresh.RefreshTokenFromRedis;
import io.mojolll.project.v1.api.user.dto.UserRequestDto;
import io.mojolll.project.v1.api.user.model.User;
import io.mojolll.project.v1.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/list")
    public List<User> users() {
        return userService.findAll();
    }

    @GetMapping("/refresh-token-list")
    public List<RefreshTokenFromRedis> refreshTokens() {
        return userService.findAllRefreshToken();
    }

    @GetMapping("/logout-token-list")
    public List<LogoutAccessTokenFromRedis> logoutTokens() {
        return userService.findAllLogoutToken();
    }

    @PostMapping(value = "/join")
    public ResponseEntity<User> signUp(@RequestBody final UserRequestDto userDto) {

        if(userService.isEmailDuplicated(userDto.getEmail())){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(userService.signUp(userDto));
    }

    @PostMapping("/login") //토큰X
    public ResponseEntity<User> login(@RequestBody final UserRequestDto userDto,
                                      HttpServletResponse response){

        User user = userService.login(userDto);
        String accessToken = TokenUtils.generateJwtAccessToken(user);
        response.setHeader(JwtProperties.ACCESS_HEADER_STRING,JwtProperties.TOKEN_PREFIX + accessToken);

        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutAccessTokenFromRedis> logout(HttpServletRequest request){
        String token = request.getHeader(JwtProperties.ACCESS_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX, "");
        return userService.logout(token);
    }

    @PostMapping("/login2") //토큰O
    public ResponseEntity<User> login(HttpServletRequest request, HttpServletResponse response){

        String token = request.getHeader(JwtProperties.ACCESS_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX, "");

        //expire 되었을 때 refresh토큰으로 발급해주기 -> response.sendRedirect(); 형이랑 말해보기
        // throws ExpiredJwtException, MalformedJwtException, SignatureException
        return userService.login2(token);
    }


    @GetMapping("/api/v1/user")
    public String user() {
        return "user";
    }

    @GetMapping("/api/v1/manager")
    public String manager() {
        return "manager";
    }

    @GetMapping("/api/v1/admin")
    public String admin() {
        return "admin";
    }

}
