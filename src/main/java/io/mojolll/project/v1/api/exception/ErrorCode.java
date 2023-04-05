package io.mojolll.project.v1.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
//    USERNAME_DUPLICATED(HttpStatus.CONFLICT,""), //409보다 400번이..
    USERNAME_DUPLICATED(HttpStatus.BAD_REQUEST,""),
    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND,""),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED,""),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED,""),
    EXPIRE_TOKEN(HttpStatus.UNAUTHORIZED,"");



    private HttpStatus httpStatus;
    private String message;

}
