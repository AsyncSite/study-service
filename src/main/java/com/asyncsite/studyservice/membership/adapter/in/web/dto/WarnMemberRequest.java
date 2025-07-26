package com.asyncsite.studyservice.membership.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "멤버 경고 요청")
public class WarnMemberRequest {
    
    @NotNull
    @Schema(description = "요청자 ID", example = "leader123")
    private String requesterId;
    
    @NotBlank
    @Schema(description = "경고 사유", example = "3회 연속 불참")
    private String reason;
}