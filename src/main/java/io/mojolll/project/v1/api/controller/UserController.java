package io.mojolll.project.v1.api.controller;

import io.mojolll.project.v1.api.config.jwt.JwtProperties;
import io.mojolll.project.v1.api.config.jwt.TokenUtils;
import io.mojolll.project.v1.api.dto.SignUpRequestDto;
import io.mojolll.project.v1.api.model.User;
import io.mojolll.project.v1.api.service.UserService;
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
    public ResponseEntity<User> signUp(@RequestBody final SignUpRequestDto signUpDTO, HttpServletResponse response) {

        if(userService.isEmailDuplicated(signUpDTO.getEmail())){
            return ResponseEntity.badRequest().build();
        }
        User user = userService.signUp(signUpDTO);
        String token = TokenUtils.generateJwtAccessToken(user);
        response.setHeader(JwtProperties.HEADER_STRING,JwtProperties.TOKEN_PREFIX + token);

        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/login") //토큰O
    public ResponseEntity<User> login(HttpServletRequest request, HttpServletResponse response){
        String header = request.getHeader(JwtProperties.HEADER_STRING);


        String[] token = header.split(" ");
        log.info("controller tokenFromHeader -> 0:{}",token[0]);
        log.info("controller tokenFromHeader -> 1:{}",token[1]);


        if (!JwtProperties.TOKEN_PREFIX.trim().equals(token[0])){
            throw new NoSuchElementException("token이 일치하지 않습니다.");
        }
        User user = userService.login(token[1]);

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
