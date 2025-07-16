package com.asyncsite.studyservice.domain.model;

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
        this.status = StudyStatus.APPROVED;
        this.updatedAt = LocalDateTime.now();
    }

    public void reject() {
        this.status = StudyStatus.REJECTED;
        this.updatedAt = LocalDateTime.now();
    }

    public void terminate() {
        this.status = StudyStatus.TERMINATED;
        this.updatedAt = LocalDateTime.now();
    }
}