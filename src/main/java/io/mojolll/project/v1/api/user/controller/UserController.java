package io.mojolll.project.v1.api.user.controller;

import io.mojolll.project.v1.api.config.jwt.JwtProperties;
import io.mojolll.project.v1.api.config.jwt.TokenUtils;
import io.mojolll.project.v1.api.user.dto.LoginRequestDto;
import io.mojolll.project.v1.api.user.dto.SignUpRequestDto;
import io.mojolll.project.v1.api.user.model.User;
import io.mojolll.project.v1.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.NoSuchElementException;

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

    @PostMapping(value = "/join")
    public ResponseEntity<User> signUp(@RequestBody final SignUpRequestDto signUpDto) {

        if(userService.isEmailDuplicated(signUpDto.getEmail())){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(userService.signUp(signUpDto));
    }

    @PostMapping("/login") //토큰X
    public ResponseEntity<User> login(@RequestBody final LoginRequestDto loginDto,
                                      HttpServletResponse response){

        User user = userService.login(loginDto);
        String token = TokenUtils.generateJwtAccessToken(user);
        log.info("controller token{} :",token);
        response.setHeader(JwtProperties.HEADER_STRING,JwtProperties.TOKEN_PREFIX + token);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/login2") //토큰O
    public ResponseEntity<User> login(HttpServletRequest request, HttpServletResponse response){
        String header = request.getHeader(JwtProperties.HEADER_STRING);

        String[] token = header.split(" ");
        log.info("controller tokenFromHeader -> 0:{}",token[0]);
        log.info("controller tokenFromHeader -> 1:{}",token[1]);


        if (!JwtProperties.TOKEN_PREFIX.trim().equals(token[0])){
            throw new NoSuchElementException("token이 일치하지 않습니다.");
        }
        User user = userService.login2(token[1]);

        return ResponseEntity.ok().body(user);
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
