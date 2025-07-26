package com.asyncsite.studyservice.config;

import com.asyncsite.coreplatform.common.dto.ApiResponse;
import com.asyncsite.coreplatform.common.dto.ErrorDetail;
import com.asyncsite.studyservice.domain.service.StudyNotFoundException;
import com.asyncsite.studyservice.domain.exception.StudyAlreadyApprovedException;
import com.asyncsite.studyservice.domain.exception.StudyAlreadyRejectedException;
import com.asyncsite.studyservice.domain.exception.StudyAlreadyTerminatedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(StudyNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleStudyNotFound(StudyNotFoundException ex) {
        log.warn("Study not found: {}", ex.getMessage());
        
        ApiResponse<Object> response = ApiResponse.error(
            "BIZ-3001", 
            "스터디를 찾을 수 없습니다.",
            null
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(StudyAlreadyApprovedException.class)
    public ResponseEntity<ApiResponse<Object>> handleStudyAlreadyApproved(StudyAlreadyApprovedException ex) {
        log.warn("Study already approved: {}", ex.getMessage());
        
        ApiResponse<Object> response = ApiResponse.error(
            "BIZ-3003",
            "이미 승인된 스터디입니다.",
            null
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(StudyAlreadyRejectedException.class)
    public ResponseEntity<ApiResponse<Object>> handleStudyAlreadyRejected(StudyAlreadyRejectedException ex) {
        log.warn("Study already rejected: {}", ex.getMessage());
        
        ApiResponse<Object> response = ApiResponse.error(
            "BIZ-3004",
            "이미 거절된 스터디입니다.",
            null
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(StudyAlreadyTerminatedException.class)
    public ResponseEntity<ApiResponse<Object>> handleStudyAlreadyTerminated(StudyAlreadyTerminatedException ex) {
        log.warn("Study already terminated: {}", ex.getMessage());
        
        ApiResponse<Object> response = ApiResponse.error(
            "BIZ-3005",
            "이미 종료된 스터디입니다.",
            null
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        log.warn("Validation failed: {}", ex.getMessage());
        
        Map<String, Object> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });
        
        Map<String, Object> details = new HashMap<>();
        details.put("validationErrors", validationErrors);
        
        ApiResponse<Object> response = ApiResponse.error(
            "VAL-2001",
            "입력값 검증에 실패했습니다.",
            details
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalState(IllegalStateException ex) {
        log.warn("Invalid state operation: {}", ex.getMessage());
        
        ApiResponse<Object> response = ApiResponse.error(
            "BIZ-3002",
            "현재 상태에서는 해당 작업을 수행할 수 없습니다.",
            null
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Invalid argument: {}", ex.getMessage());
        
        ApiResponse<Object> response = ApiResponse.error(
            "VAL-2002",
            "잘못된 입력값입니다.",
            null
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        
        ApiResponse<Object> response = ApiResponse.error(
            "SYS-5001",
            "시스템 오류가 발생했습니다.",
            null
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}