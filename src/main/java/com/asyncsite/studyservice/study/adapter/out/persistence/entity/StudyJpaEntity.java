package com.asyncsite.studyservice.study.adapter.out.persistence.entity;

import com.asyncsite.studyservice.study.domain.model.StudyStatus;
import com.asyncsite.studyservice.study.domain.model.StudyType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
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
    
    @Column(name = "generation")
    private Integer generation;
    
    @Column(name = "slug", unique = true, length = 50)
    private String slug;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 20)
    private StudyType type;
    
    @Column(name = "tagline", length = 200)
    private String tagline;
    
    @Column(name = "schedule", length = 100)
    private String schedule;
    
    @Column(name = "duration", length = 50)
    private String duration;
    
    @Column(name = "capacity")
    private Integer capacity;
    
    @Column(name = "enrolled")
    private Integer enrolled;
    
    @Column(name = "recruit_deadline")
    private LocalDate recruitDeadline;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    protected StudyJpaEntity() {
    }
    
    public StudyJpaEntity(final UUID id, final String title, final String description, 
                         final String proposerId, final StudyStatus status, 
                         final LocalDateTime createdAt, final LocalDateTime updatedAt,
                         final Integer generation, final String slug, final StudyType type,
                         final String tagline, final String schedule, final String duration,
                         final Integer capacity, final Integer enrolled, final LocalDate recruitDeadline,
                         final LocalDate startDate, final LocalDate endDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.proposerId = proposerId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.generation = generation;
        this.slug = slug;
        this.type = type;
        this.tagline = tagline;
        this.schedule = schedule;
        this.duration = duration;
        this.capacity = capacity;
        this.enrolled = enrolled;
        this.recruitDeadline = recruitDeadline;
        this.startDate = startDate;
        this.endDate = endDate;
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
    
    public Integer getGeneration() {
        return generation;
    }
    
    public String getSlug() {
        return slug;
    }
    
    public StudyType getType() {
        return type;
    }
    
    public String getTagline() {
        return tagline;
    }
    
    public String getSchedule() {
        return schedule;
    }
    
    public String getDuration() {
        return duration;
    }
    
    public Integer getCapacity() {
        return capacity;
    }
    
    public Integer getEnrolled() {
        return enrolled;
    }
    
    public LocalDate getRecruitDeadline() {
        return recruitDeadline;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setStatus(final StudyStatus status) {
        this.status = status;
    }
    
    public void setUpdatedAt(final LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public void setEnrolled(final Integer enrolled) {
        this.enrolled = enrolled;
    }
}
