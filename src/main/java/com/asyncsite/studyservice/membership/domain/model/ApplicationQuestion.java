package com.asyncsite.studyservice.membership.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationQuestion {
    private String id;
    private String question;
    private QuestionType type;
    private boolean required;
    private List<String> options; // 객관식 질문의 경우
    private Integer maxLength; // 텍스트 답변의 최대 길이
    private Integer order;
    
    public enum QuestionType {
        TEXT("주관식"),
        TEXTAREA("장문"),
        SINGLE_CHOICE("단일선택"),
        MULTIPLE_CHOICE("복수선택"),
        NUMBER("숫자"),
        DATE("날짜");
        
        private final String description;
        
        QuestionType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    // 정적 팩토리 메서드
    public static ApplicationQuestion textQuestion(String question, boolean required, int order) {
        return ApplicationQuestion.builder()
                .id(java.util.UUID.randomUUID().toString())
                .question(question)
                .type(QuestionType.TEXT)
                .required(required)
                .order(order)
                .maxLength(500)
                .build();
    }
    
    public static ApplicationQuestion textareaQuestion(String question, boolean required, int order, int maxLength) {
        return ApplicationQuestion.builder()
                .id(java.util.UUID.randomUUID().toString())
                .question(question)
                .type(QuestionType.TEXTAREA)
                .required(required)
                .order(order)
                .maxLength(maxLength)
                .build();
    }
    
    public static ApplicationQuestion singleChoiceQuestion(String question, List<String> options, boolean required, int order) {
        return ApplicationQuestion.builder()
                .id(java.util.UUID.randomUUID().toString())
                .question(question)
                .type(QuestionType.SINGLE_CHOICE)
                .options(options)
                .required(required)
                .order(order)
                .build();
    }
}