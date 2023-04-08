package io.mojolll.project.v1.api.config.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.mojolll.project.v1.api.config.auth.PrincipalDetails;
import io.mojolll.project.v1.api.exception.AppCustomException;
import io.mojolll.project.v1.api.exception.ErrorCode;
import io.mojolll.project.v1.api.redis.logout.LogoutAccessTokenRedisRepository;
import io.mojolll.project.v1.api.user.model.User;
import io.mojolll.project.v1.api.user.repositroy.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 인가
// 권한이나 인증이 필요한 특정 주소를 요청했을 때 BasicAuthenticationFilter를 무조건 타게 되어있다.
// 만약에 권한이 인증이 필요한 주소가 아니라면 이필터를 안탄다.
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;
    private LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  UserRepository userRepository, LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.logoutAccessTokenRedisRepository = logoutAccessTokenRedisRepository;
    }

    //인증이나 권한이 필요한 주소요청이 있을때 거침
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader(JwtProperties.ACCESS_HEADER_STRING);
        //헤더가 있는지 확인
        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        log.info("header:{}",header);
        String token = request.getHeader(JwtProperties.ACCESS_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX, "");



        // 토큰 검증 (이게 인증이기 때문에 AuthenticationManager도 필요 없음)
        // 내가 SecurityContext에 집적접근해서 세션을 만들때 자동으로 UserDetailsService에 있는
        // loadByUsername이 호출됨.
//        String email = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)//verify(token) 서명
//                .getClaim("username").asString(); //서명되면 username꺼내서 String으로
        String email = "";
        try {
            email = TokenUtils.getUserEmailFromAccessToken(token);
        } catch (ExpiredJwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"token 유효시간 만료");
            response.sendRedirect("/api/v1/users/reissue");
            chain.doFilter(request, response);

        } catch (Exception e){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"token이 유효하지 않습니다.");
            response.sendRedirect("/api/v1/users/login");
            chain.doFilter(request, response);
        }

        //로그아웃 토큰 있으면 안되게 수정하기
        if (email != "" && logoutAccessTokenRedisRepository.findByEmail(email).isEmpty()) {
            User user = userRepository.findByEmail(email).get();

            // 인증은 토큰 검증시 끝. 인증을 하기 위해서가 아닌 스프링 시큐리티가 수행해주는 권한 처리를 위해
            // 아래와 같이 토큰을 만들어서 Authentication 객체를 강제로 만들고 그걸 세션에 저장!
            PrincipalDetails principalDetails = new PrincipalDetails(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    principalDetails, // 나중에 컨트롤러에서 DI해서 쓸 때 사용하기 편함.
                    null, // 패스워드는 모르니까 null 처리, 어차피 지금 인증하는게 아니니까!!
                    principalDetails.getAuthorities());

            // 강제로 시큐리티의 세션에 접근하여 값 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}