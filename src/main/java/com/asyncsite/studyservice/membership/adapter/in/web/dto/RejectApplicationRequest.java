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
@Schema(description = "지원 거절 요청")
public class RejectApplicationRequest {
    
    @NotNull
    @Schema(description = "검토자 ID", example = "leader123")
    private String reviewerId;
    
    @NotBlank
    @Schema(description = "거절 사유", example = "현재 스터디 인원이 충분합니다")
    private String reason;
}