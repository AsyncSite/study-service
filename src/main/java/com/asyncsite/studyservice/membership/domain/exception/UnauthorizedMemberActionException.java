package com.asyncsite.studyservice.membership.domain.exception;

public class UnauthorizedMemberActionException extends RuntimeException {
    public UnauthorizedMemberActionException(String message) {
        super(message);
    }
}