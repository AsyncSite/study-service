package com.asyncsite.studyservice.common.exception;

/**
 * 권한이 없는 요청에 대한 예외
 */
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}