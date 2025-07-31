package com.asyncsite.studyservice.study.domain.model;

import java.util.Objects;
import java.util.UUID;

public class StudyLeader {
    private final UUID id;
    private final UUID studyId;
    private final String name;
    private final String profileImage;
    private final String welcomeMessage;
    private final String email;
    private final String githubId;
    
    public StudyLeader(UUID studyId, String name, String profileImage, 
                       String welcomeMessage, String email, String githubId) {
        this(UUID.randomUUID(), studyId, name, profileImage, welcomeMessage, email, githubId);
    }
    
    public StudyLeader(UUID id, UUID studyId, String name, String profileImage,
                       String welcomeMessage, String email, String githubId) {
        this.id = id;
        this.studyId = validateStudyId(studyId);
        this.name = validateName(name);
        this.profileImage = profileImage;
        this.welcomeMessage = welcomeMessage;
        this.email = email;
        this.githubId = githubId;
    }
    
    public UUID getId() {
        return id;
    }
    
    public UUID getStudyId() {
        return studyId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getProfileImage() {
        return profileImage;
    }
    
    public String getWelcomeMessage() {
        return welcomeMessage;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getGithubId() {
        return githubId;
    }
    
    // Validation methods
    private UUID validateStudyId(UUID studyId) {
        if (studyId == null) {
            throw new IllegalArgumentException("스터디 ID는 필수입니다.");
        }
        return studyId;
    }
    
    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("리더 이름은 필수입니다.");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("리더 이름은 100자를 초과할 수 없습니다.");
        }
        return name.trim();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudyLeader that = (StudyLeader) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "StudyLeader{" +
                "id=" + id +
                ", studyId=" + studyId +
                ", name='" + name + '\'' +
                ", githubId='" + githubId + '\'' +
                '}';
    }
}
