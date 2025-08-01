package com.asyncsite.studyservice.membership.adapter.in.web.dto;

import com.asyncsite.studyservice.membership.domain.model.Member;
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
public class MyStudyResponse {
    private UUID memberId;
    private UUID studyId;
    private String studyTitle;
    private String role;
    private LocalDateTime joinedAt;
    private boolean isActive;
    private int attendanceRate;
    private int warningCount;

    public static MyStudyResponse from(Member member, String studyTitle) {
        return MyStudyResponse.builder()
                .memberId(member.getId())
                .studyId(member.getStudyId())
                .studyTitle(studyTitle)
                .role(member.getRole().name())
                .joinedAt(member.getJoinedAt())
                .isActive(member.isActive())
                .warningCount(member.getWarningCount())
                .build();
    }
}