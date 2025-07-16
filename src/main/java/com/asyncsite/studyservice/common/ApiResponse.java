package com.asyncsite.studyservice.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "API 응답 공통 포맷")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
    @Schema(description = "성공 여부", example = "true")
    boolean success,
    
    @Schema(description = "응답 데이터")
    T data,
    
    @Schema(description = "응답 메시지", example = "성공")
    String message,
    
    @Schema(description = "에러 메시지", example = "요청 처리 중 오류가 발생했습니다.")
    String error
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, "성공", null);
    }
    
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, data, message, null);
    }
    
    public static <T> ApiResponse<T> failure(String error) {
        return new ApiResponse<>(false, null, null, error);
    }
    
    public static <T> ApiResponse<T> failure(String message, String error) {
        return new ApiResponse<>(false, null, message, error);
    }
}