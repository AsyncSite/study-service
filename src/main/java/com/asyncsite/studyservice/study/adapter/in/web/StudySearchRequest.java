package com.asyncsite.studyservice.study.adapter.in.web;

import com.asyncsite.studyservice.study.domain.model.StudyStatus;
import com.asyncsite.studyservice.study.domain.model.StudyType;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 스터디 검색 요청 DTO
 */
@Schema(description = "스터디 검색 요청")
public record StudySearchRequest(
    @Schema(description = "검색 키워드 (제목, 설명 검색)")
    String keyword,
    
    @Schema(description = "스터디 상태")
    StudyStatus status,
    
    @Schema(description = "스터디 유형")
    StudyType type,
    
    @Schema(description = "기수")
    Integer generation,
    
    @Schema(description = "모집중인 스터디만 조회")
    Boolean recruiting
) {
}