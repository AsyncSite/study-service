package com.asyncsite.studyservice.domain.exception;

public class StudyAlreadyRejectedException extends RuntimeException {
    public StudyAlreadyRejectedException(String message) {
        super(message);
    }
    
    public StudyAlreadyRejectedException(String message, Throwable cause) {
        super(message, cause);
    }
}