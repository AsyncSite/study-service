package com.asyncsite.studyservice.membership.adapter.in.web.dto;

import com.asyncsite.studyservice.membership.domain.model.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "멤버 역할 변경 요청")
public class ChangeMemberRoleRequest {
    
    @NotNull
    @Schema(description = "요청자 ID", example = "leader123")
    private String requesterId;
    
    @NotNull
    @Schema(description = "새로운 역할", example = "CO_LEADER")
    private MemberRole newRole;
}