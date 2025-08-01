package com.asyncsite.studyservice.membership.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationForm {
    private UUID id;
    private UUID studyId;
    private String title;
    private String description;
    private List<ApplicationQuestion> questions;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    
    // 비즈니스 메서드
    public void activate() {
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void deactivate() {
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void addQuestion(ApplicationQuestion question) {
        if (this.questions == null) {
            this.questions = new ArrayList<>();
        }
        this.questions.add(question);
        this.updatedAt = LocalDateTime.now();
    }
    
    public void removeQuestion(int index) {
        if (this.questions != null && index >= 0 && index < this.questions.size()) {
            this.questions.remove(index);
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void updateQuestion(int index, ApplicationQuestion newQuestion) {
        if (this.questions != null && index >= 0 && index < this.questions.size()) {
            this.questions.set(index, newQuestion);
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void updateQuestions(List<ApplicationQuestion> newQuestions) {
        this.questions = new ArrayList<>(newQuestions);
        this.updatedAt = LocalDateTime.now();
    }
    
    // 정적 팩토리 메서드
    public static ApplicationForm create(UUID studyId, String title, String description, String createdBy) {
        return ApplicationForm.builder()
                .id(UUID.randomUUID())
                .studyId(studyId)
                .title(title)
                .description(description)
                .questions(new ArrayList<>())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .createdBy(createdBy)
                .build();
    }
    
    public static ApplicationForm create(UUID studyId, List<ApplicationQuestion> questions) {
        return ApplicationForm.builder()
                .id(UUID.randomUUID())
                .studyId(studyId)
                .title("스터디 지원서")
                .description("스터디 참여를 위한 지원서입니다.")
                .questions(new ArrayList<>(questions))
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .createdBy("system")
                .build();
    }
}