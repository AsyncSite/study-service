package com.asyncsite.studyservice.common.exception;

/**
 * 인증되지 않은 요청에 대한 예외
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}