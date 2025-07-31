package com.asyncsite.studyservice.study.adapter.out.persistence.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "study_themes")
public class StudyThemeJpaEntity {
    
    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;
    
    @Column(name = "study_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID studyId;
    
    @Column(name = "primary_color", length = 7)
    private String primaryColor;
    
    @Column(name = "secondary_color", length = 7)
    private String secondaryColor;
    
    @Column(name = "glow_color", length = 30)
    private String glowColor;
    
    @Column(name = "hero_image", length = 500)
    private String heroImage;
    
    protected StudyThemeJpaEntity() {
        // JPA 전용 기본 생성자
    }
    
    public StudyThemeJpaEntity(final UUID id, final UUID studyId, final String primaryColor,
                              final String secondaryColor, final String glowColor, final String heroImage) {
        this.id = id;
        this.studyId = studyId;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.glowColor = glowColor;
        this.heroImage = heroImage;
    }
    
    public UUID getId() {
        return id;
    }
    
    public UUID getStudyId() {
        return studyId;
    }
    
    public String getPrimaryColor() {
        return primaryColor;
    }
    
    public String getSecondaryColor() {
        return secondaryColor;
    }
    
    public String getGlowColor() {
        return glowColor;
    }
    
    public String getHeroImage() {
        return heroImage;
    }
}
