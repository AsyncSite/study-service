package com.asyncsite.studyservice.study.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "스터디 상태", enumAsRef = true)
public enum StudyStatus {
    @Schema(description = "대기중")
    PENDING,
    
    @Schema(description = "승인됨")
    APPROVED,
    
    @Schema(description = "거절됨")
    REJECTED,
    
    @Schema(description = "진행중")
    IN_PROGRESS,

    @Schema(description = "완료됨")
    COMPLETED,

    @Schema(description = "종료됨")
    TERMINATED
}
