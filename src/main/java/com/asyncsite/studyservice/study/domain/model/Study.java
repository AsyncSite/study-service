package com.asyncsite.studyservice.study.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Study {
    private final UUID id;
    private final String title;
    private final String description;
    private final String proposerId;
    private StudyStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Study(String title, String description, String proposerId) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.proposerId = proposerId;
        this.status = StudyStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Study(UUID id, String title, String description, String proposerId, StudyStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.proposerId = proposerId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getProposerId() {
        return proposerId;
    }

    public StudyStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void approve() {
        if (this.status == StudyStatus.APPROVED) {
            throw new com.asyncsite.studyservice.study.domain.exception.StudyAlreadyApprovedException("스터디가 이미 승인되었습니다.");
        }
        this.status = StudyStatus.APPROVED;
        this.updatedAt = LocalDateTime.now();
    }

    public void reject() {
        if (this.status == StudyStatus.REJECTED) {
            throw new com.asyncsite.studyservice.study.domain.exception.StudyAlreadyRejectedException("스터디가 이미 거절되었습니다.");
        }
        this.status = StudyStatus.REJECTED;
        this.updatedAt = LocalDateTime.now();
    }

    public void terminate() {
        if (this.status == StudyStatus.TERMINATED) {
            throw new com.asyncsite.studyservice.study.domain.exception.StudyAlreadyTerminatedException("스터디가 이미 종료되었습니다.");
        }
        this.status = StudyStatus.TERMINATED;
        this.updatedAt = LocalDateTime.now();
    }
}
