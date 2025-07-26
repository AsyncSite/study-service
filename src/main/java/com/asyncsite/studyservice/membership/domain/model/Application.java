package com.asyncsite.studyservice.membership.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Application {
    private UUID id;
    private UUID studyId;
    private String applicantId;
    private ApplicationStatus status;
    private Map<String, Object> answers;
    private String introduction;
    private String rejectionReason;
    private LocalDateTime appliedAt;
    private LocalDateTime processedAt;
    private String processedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String reviewNote;
    private String reviewedBy;
    
    // 비즈니스 메서드
    public boolean isPending() {
        return status == ApplicationStatus.PENDING;
    }
    
    public boolean isProcessed() {
        return status == ApplicationStatus.ACCEPTED || status == ApplicationStatus.REJECTED;
    }
    
    public void accept(String acceptedBy) {
        accept(acceptedBy, null);
    }
    
    public void accept(String acceptedBy, String note) {
        if (!isPending()) {
            throw new IllegalStateException("이미 처리된 지원서입니다.");
        }
        this.status = ApplicationStatus.ACCEPTED;
        this.processedAt = LocalDateTime.now();
        this.processedBy = acceptedBy;
        this.reviewedBy = acceptedBy;
        this.reviewNote = note;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void reject(String rejectedBy, String reason) {
        if (!isPending()) {
            throw new IllegalStateException("이미 처리된 지원서입니다.");
        }
        this.status = ApplicationStatus.REJECTED;
        this.rejectionReason = reason;
        this.processedAt = LocalDateTime.now();
        this.processedBy = rejectedBy;
    }
    
    public void cancel() {
        if (!isPending()) {
            throw new IllegalStateException("대기 중인 지원서만 취소할 수 있습니다.");
        }
        this.status = ApplicationStatus.CANCELLED;
        this.processedAt = LocalDateTime.now();
    }
    
    // 정적 팩토리 메서드
    public static Application create(UUID studyId, String applicantId, Map<String, Object> answers, String introduction) {
        LocalDateTime now = LocalDateTime.now();
        return Application.builder()
                .id(UUID.randomUUID())
                .studyId(studyId)
                .applicantId(applicantId)
                .status(ApplicationStatus.PENDING)
                .answers(answers)
                .introduction(introduction)
                .appliedAt(now)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }
}