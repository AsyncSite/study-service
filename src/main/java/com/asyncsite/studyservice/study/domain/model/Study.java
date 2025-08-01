package com.asyncsite.studyservice.study.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Study {
    private final UUID id;
    private final String title;
    private final String description;
    private final String proposerId;
    private StudyStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private final Integer generation;
    private final String slug;
    private final StudyType type;
    private final String tagline;
    private final String schedule;
    private final String duration;
    private final Integer capacity;
    private Integer enrolled;
    private final LocalDate recruitDeadline;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Study(String title, String description, String proposerId) {
        this(UUID.randomUUID(), title, description, proposerId, StudyStatus.PENDING,
                LocalDateTime.now(), LocalDateTime.now(), null, null, null, null,
                null, null, null, 0, null, null, null);
    }

    public Study(String title, String description, String proposerId, Integer generation,
                 String slug, StudyType type, String tagline, String schedule, String duration,
                 Integer capacity, LocalDate recruitDeadline, LocalDate startDate, LocalDate endDate) {
        this(UUID.randomUUID(), title, description, proposerId, StudyStatus.PENDING,
                LocalDateTime.now(), LocalDateTime.now(), generation, slug, type, tagline,
                schedule, duration, capacity, 0, recruitDeadline, startDate, endDate);
    }

    public Study(UUID id, String title, String description, String proposerId, StudyStatus status,
                 LocalDateTime createdAt, LocalDateTime updatedAt, Integer generation, String slug,
                 StudyType type, String tagline, String schedule, String duration, Integer capacity,
                 Integer enrolled, LocalDate recruitDeadline, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.title = validateTitle(title);
        this.description = validateDescription(description);
        this.proposerId = validateProposerId(proposerId);
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.generation = generation;
        this.slug = slug;
        this.type = type;
        this.tagline = tagline;
        this.schedule = schedule;
        this.duration = duration;
        this.capacity = capacity != null ? validateCapacity(capacity) : null;
        this.enrolled = enrolled != null ? enrolled : 0;
        this.recruitDeadline = recruitDeadline;
        this.startDate = startDate;
        this.endDate = endDate;

        validateDateRange();
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

    public void approve() {
        if (this.status == StudyStatus.APPROVED) {
            throw new com.asyncsite.studyservice.study.domain.exception.StudyAlreadyApprovedException("스터디가 이미 승인되었습니다.");
        }
        if (!canBeApproved()) {
            throw new IllegalStateException("승인할 수 없는 상태입니다: " + this.status);
        }
        this.status = StudyStatus.APPROVED;
        this.updatedAt = LocalDateTime.now();
    }

    public void reject() {
        if (this.status == StudyStatus.REJECTED) {
            throw new com.asyncsite.studyservice.study.domain.exception.StudyAlreadyRejectedException("스터디가 이미 거절되었습니다.");
        }
        if (!canBeRejected()) {
            throw new IllegalStateException("거절할 수 없는 상태입니다: " + this.status);
        }
        this.status = StudyStatus.REJECTED;
        this.updatedAt = LocalDateTime.now();
    }

    public void start() {
        if (this.status != StudyStatus.APPROVED) {
            throw new IllegalStateException("승인된 스터디만 시작할 수 있습니다.");
        }
        this.status = StudyStatus.IN_PROGRESS;
        this.updatedAt = LocalDateTime.now();
    }

    public void complete() {
        if (this.status != StudyStatus.IN_PROGRESS) {
            throw new IllegalStateException("진행중인 스터디만 완료할 수 있습니다.");
        }
        this.status = StudyStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }

    public void terminate() {
        if (this.status == StudyStatus.TERMINATED) {
            throw new com.asyncsite.studyservice.study.domain.exception.StudyAlreadyTerminatedException("스터디가 이미 종료되었습니다.");
        }
        if (this.status == StudyStatus.COMPLETED) {
            throw new IllegalStateException("이미 완료된 스터디는 종료할 수 없습니다.");
        }
        this.status = StudyStatus.TERMINATED;
        this.updatedAt = LocalDateTime.now();
    }

    public void increaseEnrolled() {
        if (this.capacity != null && this.enrolled >= this.capacity) {
            throw new IllegalStateException("정원이 초과되었습니다.");
        }
        this.enrolled++;
        this.updatedAt = LocalDateTime.now();
    }

    public void decreaseEnrolled() {
        if (this.enrolled <= 0) {
            throw new IllegalStateException("참여자 수가 0 이하입니다.");
        }
        this.enrolled--;
        this.updatedAt = LocalDateTime.now();
    }

    // Validation methods
    private String validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("스터디 제목은 필수입니다.");
        }
        if (title.length() > 100) {
            throw new IllegalArgumentException("스터디 제목은 100자를 초과할 수 없습니다.");
        }
        return title.trim();
    }

    private String validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("스터디 설명은 필수입니다.");
        }
        if (description.length() > 1000) {
            throw new IllegalArgumentException("스터디 설명은 1000자를 초과할 수 없습니다.");
        }
        return description.trim();
    }

    private String validateProposerId(String proposerId) {
        if (proposerId == null || proposerId.trim().isEmpty()) {
            throw new IllegalArgumentException("스터디 제안자 ID는 필수입니다.");
        }
        return proposerId.trim();
    }

    private Integer validateCapacity(Integer capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("정원은 0보다 커야 합니다.");
        }
        if (capacity > 100) {
            throw new IllegalArgumentException("정원은 100명을 초과할 수 없습니다.");
        }
        return capacity;
    }

    private void validateDateRange() {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("시작일은 종료일보다 빠를 수 없습니다.");
        }
        if (recruitDeadline != null && startDate != null && recruitDeadline.isAfter(startDate)) {
            throw new IllegalArgumentException("모집 마감일은 시작일보다 빠를 수 없습니다.");
        }
    }

    private boolean canBeApproved() {
        return this.status == StudyStatus.PENDING;
    }

    private boolean canBeRejected() {
        return this.status == StudyStatus.PENDING;
    }

    // Status check methods
    public boolean isActive() {
        return this.status == StudyStatus.IN_PROGRESS || this.status == StudyStatus.APPROVED;
    }

    public boolean isPending() {
        return this.status == StudyStatus.PENDING;
    }

    public boolean isCompleted() {
        return this.status == StudyStatus.COMPLETED;
    }

    public boolean isTerminated() {
        return this.status == StudyStatus.TERMINATED;
    }

    public boolean isRecruiting() {
        return this.status == StudyStatus.APPROVED &&
               (this.recruitDeadline == null || !this.recruitDeadline.isBefore(LocalDate.now())) &&
               (this.capacity == null || this.enrolled < this.capacity);
    }

    public boolean isFull() {
        return this.capacity != null && this.enrolled >= this.capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Study study = (Study) o;
        return Objects.equals(id, study.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Study{" +
               "id=" + id +
               ", title='" + title + '\'' +
               ", slug='" + slug + '\'' +
               ", status=" + status +
               ", generation=" + generation +
               ", enrolled=" + enrolled +
               ", capacity=" + capacity +
               '}';
    }
}