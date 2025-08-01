package com.asyncsite.studyservice.membership.adapter.in.web.dto;

import com.asyncsite.studyservice.membership.domain.model.ApplicationForm;
import com.asyncsite.studyservice.membership.domain.model.ApplicationQuestion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "지원서 양식 응답")
public class ApplicationFormResponse {
    
    @Schema(description = "양식 ID")
    private UUID id;
    
    @Schema(description = "스터디 ID")
    private UUID studyId;
    
    @Schema(description = "질문 목록")
    private List<QuestionDto> questions;
    
    @Schema(description = "생성일시")
    private LocalDateTime createdAt;
    
    @Schema(description = "수정일시")
    private LocalDateTime updatedAt;
    
    @Schema(description = "활성 상태")
    private boolean isActive;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "질문 정보")
    public static class QuestionDto {
        
        @Schema(description = "질문 ID")
        private String id;
        
        @Schema(description = "질문 내용")
        private String content;
        
        @Schema(description = "질문 유형")
        private ApplicationQuestion.QuestionType type;
        
        @Schema(description = "필수 여부")
        private boolean isRequired;
        
        @Schema(description = "선택지 목록")
        private List<String> options;
        
        public static QuestionDto from(ApplicationQuestion question) {
            return QuestionDto.builder()
                    .id(question.getId())
                    .content(question.getQuestion())
                    .type(question.getType())
                    .isRequired(question.isRequired())
                    .options(question.getOptions())
                    .build();
        }
    }
    
    public static ApplicationFormResponse from(ApplicationForm form) {
        return ApplicationFormResponse.builder()
                .id(form.getId())
                .studyId(form.getStudyId())
                .questions(form.getQuestions().stream()
                        .map(QuestionDto::from)
                        .collect(Collectors.toList()))
                .createdAt(form.getCreatedAt())
                .updatedAt(form.getUpdatedAt())
                .isActive(form.isActive())
                .build();
    }
}