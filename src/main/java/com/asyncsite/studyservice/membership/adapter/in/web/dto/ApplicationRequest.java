package com.asyncsite.studyservice.membership.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "스터디 지원 요청")
public class ApplicationRequest {
    
    @NotNull
    @Schema(description = "지원자 ID", example = "user123")
    private String applicantId;
    
    @NotNull
    @Schema(description = "지원서 답변", example = "{\"question1\": \"answer1\", \"question2\": \"answer2\"}")
    private Map<String, String> answers;
}