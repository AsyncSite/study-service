package com.asyncsite.studyservice.common;

public record AppError(
    String code,
    String message
) {
    public static AppError of(String code, String message) {
        return new AppError(code, message);
    }
    
    public static AppError studyNotFound(String studyId) {
        return new AppError("STUDY_NOT_FOUND", "스터디를 찾을 수 없습니다: " + studyId);
    }
    
    public static AppError invalidStudyStatus(String currentStatus, String requiredStatus) {
        return new AppError("INVALID_STUDY_STATUS", 
            String.format("스터디 상태가 올바르지 않습니다. 현재: %s, 필요: %s", currentStatus, requiredStatus));
    }
    
    public static AppError unauthorized(String action) {
        return new AppError("UNAUTHORIZED", "권한이 없습니다: " + action);
    }
}