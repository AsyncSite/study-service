package com.asyncsite.studyservice.study.domain.service;

public class StudyNotFoundException extends RuntimeException {
    public StudyNotFoundException(String message) {
        super(message);
    }
}
