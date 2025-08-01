package com.asyncsite.studyservice.study.domain.model;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public class StudyTheme {
    private static final Pattern COLOR_PATTERN = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");
    
    private final UUID id;
    private final UUID studyId;
    private final String primaryColor;
    private final String secondaryColor;
    private final String glowColor;
    private final String heroImage;
    
    public StudyTheme(UUID studyId, String primaryColor, String secondaryColor, 
                      String glowColor, String heroImage) {
        this(UUID.randomUUID(), studyId, primaryColor, secondaryColor, glowColor, heroImage);
    }
    
    public StudyTheme(UUID id, UUID studyId, String primaryColor, String secondaryColor,
                      String glowColor, String heroImage) {
        this.id = id;
        this.studyId = validateStudyId(studyId);
        this.primaryColor = validateColor(primaryColor, "Primary color");
        this.secondaryColor = validateColor(secondaryColor, "Secondary color");
        this.glowColor = glowColor; // glow color는 rgba 형식도 허용하므로 별도 검증 없음
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
    
    // Validation methods
    private UUID validateStudyId(UUID studyId) {
        if (studyId == null) {
            throw new IllegalArgumentException("스터디 ID는 필수입니다.");
        }
        return studyId;
    }
    
    private String validateColor(String color, String fieldName) {
        if (color != null && !COLOR_PATTERN.matcher(color).matches()) {
            throw new IllegalArgumentException(fieldName + "는 유효한 HEX 색상 코드여야 합니다. (예: #FF0000)");
        }
        return color;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudyTheme that = (StudyTheme) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "StudyTheme{" +
                "id=" + id +
                ", studyId=" + studyId +
                ", primaryColor='" + primaryColor + '\'' +
                ", secondaryColor='" + secondaryColor + '\'' +
                '}';
    }
}
