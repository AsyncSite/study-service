package com.asyncsite.studyservice.adapter.in.web;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "스터디 제안 요청")
public record StudyCreateRequest(
    @Schema(description = "스터디 제목", example = "Spring Boot 완전 정복", maxLength = 100)
    @NotBlank(message = "제목은 필수입니다")
    @Size(max = 100, message = "제목은 100자 이내여야 합니다")
    String title,

    @Schema(description = "스터디 설명", example = "Spring Boot 프레임워크를 깊이 있게 학습하는 스터디", maxLength = 500)
    @NotBlank(message = "설명은 필수입니다")
    @Size(max = 500, message = "설명은 500자 이내여야 합니다")
    String description,

    @Schema(description = "제안자 ID", example = "user001")
    @NotBlank(message = "제안자 ID는 필수입니다")
    String proposerId
) {
}