package io.mojolll.project.v1.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor //메시지 처리를 위해서
@Getter
public class AppCustomException extends RuntimeException{
    private ErrorCode errorCode;
    private final String message;
}
