package com.asyncsite.studyservice.study.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "스터디 유형", enumAsRef = true)
public enum StudyType {
    @Schema(description = "참여형 - 구성원이 적극적으로 참여하는 스터디")
    PARTICIPATORY,
    
    @Schema(description = "교육형 - 강의나 학습 위주의 스터디")
    EDUCATIONAL
}