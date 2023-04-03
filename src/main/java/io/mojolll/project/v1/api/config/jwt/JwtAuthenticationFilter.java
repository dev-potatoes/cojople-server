package io.mojolll.project.v1.api.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mojolll.project.v1.api.config.auth.PrincipalDetails;
import io.mojolll.project.v1.api.user.dto.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// .formLogin().disable() 해서 동작안하는데 security config에 JwtAuthenticationFilter 등록하면 동작함
//스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 가 있다.
// login (POST)요청해서 email, password 전송하면 UsernamePasswordAuthenticationFilter 동작을 한다.
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // authenticationManager를 통해서 로그인시도 하기위한 메서드 (/login 요청을 하면 로그인 시도를 위해서 실행되는 함수)
    // Authentication 객체 만들어서 리턴 => 의존 : AuthenticationManager
    // 인증 요청시에 실행되는 함수 => /login
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        log.info("JwtAuthenticationFilter : 진입");


        // request에 있는 username과 password를 파싱해서 자바 Object로 받기
        ObjectMapper om = new ObjectMapper();
        LoginRequestDto loginRequestDto = null;
        try {
            loginRequestDto = om.readValue(request.getInputStream(), LoginRequestDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("JwtAuthenticationFilter{}:",loginRequestDto);

        // 유저네임패스워드 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword());

        log.info("JwtAuthenticationFilter : 토큰생성완료");

        //PrincipalDetails를 세션에 담는 이유 -> 권한 관리를 위해서 아니면 hasRole()동작안함

        // authenticate() 함수가 호출 되면 인증 프로바이더가 유저 디테일 서비스의 (PrincipalDetailsService)
        // loadUserByUsername(토큰의 첫번째 파라메터) 를 호출하고
        // UserDetails를 리턴받아서 토큰의 두번째 파라메터(credential)과 (return new PrincipalDetails(user);)
        // UserDetails(DB값)의 getPassword()함수로 비교해서 동일하면
        // Authentication 객체를 만들어서 필터체인으로 리턴해준다.


        // Tip: 인증 프로바이더의 디폴트 서비스는 UserDetailsService 타입
        // Tip: 인증 프로바이더의 디폴트 암호화 방식은 BCryptPasswordEncoder
        // 결론은 인증 프로바이더에게 알려줄 필요가 없음.
        Authentication authentication =
                authenticationManager.authenticate(authenticationToken);
        //PrincipalDetailsService의 loadUserByUsername() 함수가 실행된 후 정상이면 authentication이 리턴된다.
        //DB에 있는 email과 password가 일치한다.


        PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal(); //object 반환
        log.info("Authentication:{}",principalDetailis.getUser().getEmail());
        //authentication 객체가 session영역에 저장을 해야하고 그 방법이 return 해주면 된다.
        //리턴의 이유는 권한관리를 security가 대신 해주기 때문에 편하려고 한다.
        //굳이 JWT 토큰을 사용하면서 세션을 만들 이유가 없지만 권한 처리때문에 session에 넣어 준다.
        return authentication;
    }

    // 위에 attemptAuthentication()실행 후 인증이 정상적으로 되었으면 successfulAuthentication()이 호출된다.
    // JWT Token 생성해서 response에 담아주기
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        PrincipalDetails principalDetailis = (PrincipalDetails) authResult.getPrincipal();

        String token = TokenUtils.generateJwtAccessToken(principalDetailis.getUser());
        response.setHeader(JwtProperties.HEADER_STRING,JwtProperties.TOKEN_PREFIX + token);
    }

}
