package com.asyncsite.studyservice.study.adapter.out.persistence.entity;

import com.asyncsite.studyservice.study.domain.model.StudyStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "studies")
public class StudyJpaEntity {
    
    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;
    
    @Column(name = "title", nullable = false, length = 255)
    private String title;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "proposer_id", nullable = false, length = 100)
    private String proposerId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StudyStatus status;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    protected StudyJpaEntity() {
        // JPA 전용 기본 생성자
    }
    
    public StudyJpaEntity(final UUID id, final String title, final String description, 
                         final String proposerId, final StudyStatus status, 
                         final LocalDateTime createdAt, final LocalDateTime updatedAt) {
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
    
    public void setStatus(final StudyStatus status) {
        this.status = status;
    }
    
    public void setUpdatedAt(final LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}