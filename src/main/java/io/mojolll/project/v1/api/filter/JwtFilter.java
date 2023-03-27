package io.mojolll.project.v1.api.filter;

import io.mojolll.project.v1.api.entity.UserRole;
import io.mojolll.project.v1.api.service.MemberService;
import io.mojolll.project.v1.api.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

//JWT이기 때문에 매번 토큰이 있는지 체크해야 된다.
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final MemberService memberService;
    private final String secretKey;

    @Override //여기로 통하면 권한을 부여가능 글쓰기 같은거
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //헤더에서 토큰 꺼내기
        String authentication = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("Authorization : {}",authentication);

        //여기 수정하기 filterChain은 가야됨 token 안보내면 블럭
        if (authentication == null || !authentication.startsWith("Bearer ")){
            log.info("Authorization을 잘못 보냈습니다.");
            filterChain.doFilter(request,response);
            return;
        }
        //Token 꺼내기
        String token = JwtUtil.getTokenFromHeader(authentication);

        //Token Expired 되었는지 여부
        if (JwtUtil.isExpired(token,secretKey)){
            log.info("Token이 만료 되었습니다.");
            filterChain.doFilter(request,response);
            return;
        }

        //Token으로 email 꺼내기
        String userEmail = JwtUtil.getUserEmail(token,secretKey);
        log.info("email:{}",userEmail);
        //Token으로 role 꺼내기
        String userRole = JwtUtil.getRoleFromToken(token, secretKey);
        log.info("role:{}",userRole);

        //권한 부여
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userEmail,null,
                        //DB에서 Role같은 것들을 지정했으면 거기서 꺼내서 넣기 UserDetail
                        List.of(new SimpleGrantedAuthority(userRole)));

        // Detail을 넣어준다.
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //request 안쪽에 인증이 되었다고 도장이 찍히는것이다.
        filterChain.doFilter(request,response);
    }
}
