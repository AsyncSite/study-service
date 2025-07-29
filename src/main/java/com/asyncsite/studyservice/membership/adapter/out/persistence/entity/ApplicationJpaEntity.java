package com.asyncsite.studyservice.membership.adapter.out.persistence.entity;

import com.asyncsite.studyservice.membership.domain.model.ApplicationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "application")
@Getter
public class ApplicationJpaEntity {
    @Id
    private UUID id;
    private UUID studyId;
    private String applicantId;
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
    private String answers;
    private String introduction;
    private String rejectionReason;
    private LocalDateTime appliedAt;
    private LocalDateTime processedAt;
    private String processedBy;
    private String reviewNote;
    private String reviewedBy;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected ApplicationJpaEntity() {}

    public ApplicationJpaEntity(
        final UUID id,
        final UUID studyId,
        final String applicantId,
        final ApplicationStatus status,
        final String answers,
        final String introduction,
        final String rejectionReason,
        final LocalDateTime appliedAt,
        final LocalDateTime processedAt,
        final String processedBy,
        final String reviewNote,
        final String reviewedBy,
        final LocalDateTime createdAt,
        final LocalDateTime updatedAt
    ) {
        this.id = id;
        this.studyId = studyId;
        this.applicantId = applicantId;
        this.status = status;
        this.answers = answers;
        this.introduction = introduction;
        this.rejectionReason = rejectionReason;
        this.appliedAt = appliedAt;
        this.processedAt = processedAt;
        this.processedBy = processedBy;
        this.reviewNote = reviewNote;
        this.reviewedBy = reviewedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}