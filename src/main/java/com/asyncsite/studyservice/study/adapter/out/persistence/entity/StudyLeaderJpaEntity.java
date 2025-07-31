package com.asyncsite.studyservice.study.adapter.out.persistence.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "study_leaders")
public class StudyLeaderJpaEntity {
    
    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;
    
    @Column(name = "study_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID studyId;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "profile_image", length = 500)
    private String profileImage;
    
    @Column(name = "welcome_message", columnDefinition = "TEXT")
    private String welcomeMessage;
    
    @Column(name = "email", length = 255)
    private String email;
    
    @Column(name = "github_id", length = 100)
    private String githubId;
    
    protected StudyLeaderJpaEntity() {
        // JPA 전용 기본 생성자
    }
    
    public StudyLeaderJpaEntity(final UUID id, final UUID studyId, final String name,
                               final String profileImage, final String welcomeMessage,
                               final String email, final String githubId) {
        this.id = id;
        this.studyId = studyId;
        this.name = name;
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
}
