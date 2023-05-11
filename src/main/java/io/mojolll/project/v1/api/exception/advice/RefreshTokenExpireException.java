package io.mojolll.project.v1.api.exception.advice;

public class RefreshTokenExpireException extends RuntimeException{
    public RefreshTokenExpireException() {
    }

    public RefreshTokenExpireException(String message) {
        super(message);
    }

    public RefreshTokenExpireException(String message, Throwable cause) {
        super(message, cause);
    }

    public RefreshTokenExpireException(Throwable cause) {
        super(cause);
    }

    public RefreshTokenExpireException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
