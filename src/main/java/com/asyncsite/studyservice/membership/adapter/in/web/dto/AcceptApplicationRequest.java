package com.asyncsite.studyservice.membership.adapter.in.web.dto;

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
@Schema(description = "지원 승인 요청")
public class AcceptApplicationRequest {
    
    @NotNull
    @Schema(description = "검토자 ID", example = "leader123")
    private String reviewerId;
    
    @Schema(description = "승인 메모", example = "좋은 지원서입니다. 환영합니다!")
    private String note;
}