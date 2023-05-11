package io.mojolll.project.v1.api.exception.advice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.mojolll.project.v1.api.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "io.mojolll.project.v1.api.user.controller")
public class ExceptionManager {


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ErrorResult usernameNotFoundHandler (UsernameNotFoundException e){
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("USER-NOT-EX", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateEmailException.class)
    public ErrorResult duplicateEmailExceptionHadnler (DuplicateEmailException e){
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("USER-DUPE-EX", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadCredentialsException.class)
    public ErrorResult badCredentialHandler (BadCredentialsException e){
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("PW-EX", e.getMessage());
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TokenNotFoundException.class)
    public ErrorResult tokenNotFoundHandler (TokenNotFoundException e){
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("TOKEN-NOT-EX", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LogoutTokenException.class)
    public ErrorResult logoutTokenExceptionHandler (LogoutTokenException e){
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("LOGOUT-EX", e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }
}
