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
public class MemberResponse {
    private UUID id;
    private UUID studyId;
    private String userId;
    private String role;
    private LocalDateTime joinedAt;
    private LocalDateTime leftAt;
    private boolean isActive;
    private int warningCount;

    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .studyId(member.getStudyId())
                .userId(member.getUserId())
                .role(member.getRole().name())
                .joinedAt(member.getJoinedAt())
                .leftAt(member.getLeftAt())
                .isActive(member.isActive())
                .warningCount(member.getWarningCount())
                .build();
    }
}