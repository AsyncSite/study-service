package com.asyncsite.studyservice.study.domain.exception;

public class StudyAlreadyApprovedException extends RuntimeException {
    public StudyAlreadyApprovedException(String message) {
        super(message);
    }
    
    public StudyAlreadyApprovedException(String message, Throwable cause) {
        super(message, cause);
    }
}
