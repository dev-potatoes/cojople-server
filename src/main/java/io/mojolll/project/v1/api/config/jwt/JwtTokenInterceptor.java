package io.mojolll.project.v1.api.config.jwt;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class JwtTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws IOException {
        log.info("JwtTokenInterceptor : 진입");
        final String header = request.getHeader(JwtProperties.HEADER_STRING);

        if (header != null) {
            final String token = TokenUtils.getTokenFromHeader(header);
            if (TokenUtils.isValidToken(token)) {
                return true;
            }
        }
        log.info("JwtTokenInterceptor : false");
        response.sendRedirect("/error/unauthorized");
        return false;

    }

}