package com.asyncsite.studyservice.study.adapter.in.web;

import com.asyncsite.studyservice.study.domain.model.StudyStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "스터디 응답")
public record StudyResponse(
    @Schema(description = "스터디 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID id,
    
    @Schema(description = "스터디 제목", example = "Spring Boot 완전 정복")
    String title,
    
    @Schema(description = "스터디 설명", example = "Spring Boot 프레임워크를 깊이 있게 학습하는 스터디")
    String description,
    
    @Schema(description = "제안자 ID", example = "user001")
    String proposerId,
    
    @Schema(description = "스터디 상태", example = "PENDING")
    StudyStatus status,
    
    @Schema(description = "생성일시", example = "2024-01-01T10:00:00")
    LocalDateTime createdAt,
    
    @Schema(description = "수정일시", example = "2024-01-01T10:00:00")
    LocalDateTime updatedAt
) {
}
