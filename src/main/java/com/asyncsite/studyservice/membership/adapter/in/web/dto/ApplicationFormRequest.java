package com.asyncsite.studyservice.membership.adapter.in.web.dto;

import com.asyncsite.studyservice.membership.domain.model.ApplicationForm;
import com.asyncsite.studyservice.membership.domain.model.ApplicationQuestion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "지원서 양식 요청")
public class ApplicationFormRequest {
    
    @NotEmpty
    @Schema(description = "질문 목록")
    private List<QuestionDto> questions;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "질문 정보")
    public static class QuestionDto {
        
        @NotNull
        @Schema(description = "질문 내용", example = "자기소개를 해주세요")
        private String content;
        
        @NotNull
        @Schema(description = "질문 유형", example = "TEXT")
        private ApplicationQuestion.QuestionType type;
        
        @Schema(description = "필수 여부", example = "true")
        private boolean isRequired;
        
        @Schema(description = "선택지 목록 (객관식인 경우)")
        private List<String> options;
        
        public ApplicationQuestion toQuestion() {
            return ApplicationQuestion.builder()
                    .id(java.util.UUID.randomUUID().toString())
                    .question(content)
                    .type(type)
                    .required(isRequired)
                    .options(options)
                    .build();
        }
    }
    
    public List<ApplicationQuestion> toQuestions() {
        return questions.stream()
                .map(QuestionDto::toQuestion)
                .collect(Collectors.toList());
    }
}