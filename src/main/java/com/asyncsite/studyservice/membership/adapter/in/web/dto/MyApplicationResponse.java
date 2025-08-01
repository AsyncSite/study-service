package com.asyncsite.studyservice.membership.adapter.in.web.dto;

import com.asyncsite.studyservice.membership.domain.model.Application;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyApplicationResponse {
    private UUID applicationId;
    private UUID studyId;
    private String studyTitle;
    private String status;
    private LocalDateTime appliedAt;
    private String reviewNote;

    public static MyApplicationResponse from(Application application, String studyTitle) {
        return MyApplicationResponse.builder()
                .applicationId(application.getId())
                .studyId(application.getStudyId())
                .studyTitle(studyTitle)
                .status(application.getStatus().name())
                .appliedAt(application.getAppliedAt())
                .reviewNote(application.getReviewNote())
                .build();
    }
}