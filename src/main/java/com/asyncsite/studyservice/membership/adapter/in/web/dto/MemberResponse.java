package com.asyncsite.studyservice.membership.adapter.in.web.dto;

import com.asyncsite.studyservice.membership.domain.model.Member;
import com.asyncsite.studyservice.membership.domain.model.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "스터디 멤버 응답")
public class MemberResponse {
    
    @Schema(description = "멤버 ID")
    private UUID id;
    
    @Schema(description = "스터디 ID")
    private UUID studyId;
    
    @Schema(description = "사용자 ID")
    private String userId;
    
    @Schema(description = "멤버 역할")
    private MemberRole role;
    
    @Schema(description = "가입일시")
    private LocalDateTime joinedAt;
    
    @Schema(description = "탈퇴일시")
    private LocalDateTime leftAt;
    
    @Schema(description = "활동 상태")
    private boolean isActive;
    
    @Schema(description = "경고 횟수")
    private int warningCount;
    
    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .studyId(member.getStudyId())
                .userId(member.getUserId())
                .role(member.getRole())
                .joinedAt(member.getJoinedAt())
                .leftAt(member.getLeftAt())
                .isActive(member.isActive())
                .warningCount(member.getWarningCount())
                .build();
    }
}