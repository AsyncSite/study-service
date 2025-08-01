package com.asyncsite.studyservice.membership.adapter.in.web.dto;

import com.asyncsite.studyservice.membership.domain.model.Application;
import com.asyncsite.studyservice.membership.domain.model.ApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "스터디 지원 응답")
public class ApplicationResponse {
    
    @Schema(description = "지원서 ID")
    private UUID id;
    
    @Schema(description = "스터디 ID")
    private UUID studyId;
    
    @Schema(description = "지원자 ID")
    private String applicantId;
    
    @Schema(description = "지원서 답변")
    private Map<String, String> answers;
    
    @Schema(description = "지원 상태")
    private ApplicationStatus status;
    
    @Schema(description = "생성일시")
    private LocalDateTime createdAt;
    
    @Schema(description = "수정일시")
    private LocalDateTime updatedAt;
    
    @Schema(description = "검토 메모")
    private String reviewNote;
    
    @Schema(description = "검토자 ID")
    private String reviewedBy;
    
    public static ApplicationResponse from(Application application) {
        return ApplicationResponse.builder()
                .id(application.getId())
                .studyId(application.getStudyId())
                .applicantId(application.getApplicantId())
                .answers(application.getAnswers())
                .status(application.getStatus())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .reviewNote(application.getReviewNote())
                .reviewedBy(application.getReviewedBy())
                .build();
    }
    
    public static ApplicationResponse createMock(UUID studyId, String applicantId) {
        return ApplicationResponse.builder()
                .id(UUID.randomUUID())
                .studyId(studyId)
                .applicantId(applicantId)
                .answers(Map.of(
                        "motivation", "함께 배우고 성장하고 싶습니다",
                        "experience", "프로그래밍 경험 2년",
                        "availability", "주 2회 참석 가능"
                ))
                .status(ApplicationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}