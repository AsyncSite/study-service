package com.asyncsite.studyservice.study.domain.exception;

public class StudyAlreadyTerminatedException extends RuntimeException {
    public StudyAlreadyTerminatedException(String message) {
        super(message);
    }
    
    public StudyAlreadyTerminatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
