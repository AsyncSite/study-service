package com.asyncsite.studyservice.study.adapter.in.web;

import com.asyncsite.coreplatform.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@Tag(name = "Study API", description = "스터디 관리 API")
public interface StudyControllerDocs {

    @Operation(summary = "스터디 제안", description = "새로운 스터디를 제안합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "스터디 제안 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    ApiResponse<StudyResponse> propose(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "스터디 제안 정보")
            @Valid @RequestBody StudyCreateRequest request);

    @Operation(summary = "스터디 승인", description = "제안된 스터디를 승인합니다. (운영자 권한 필요)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스터디 승인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스터디를 찾을 수 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음")
    })
    ApiResponse<StudyResponse> approve(
            @Parameter(description = "스터디 ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID studyId);

    @Operation(summary = "스터디 거절", description = "제안된 스터디를 거절합니다. (운영자 권한 필요)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스터디 거절 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스터디를 찾을 수 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음")
    })
    ApiResponse<StudyResponse> reject(
            @Parameter(description = "스터디 ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID studyId);

    @Operation(summary = "스터디 종료", description = "진행 중인 스터디를 종료합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스터디 종료 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스터디를 찾을 수 없음")
    })
    ApiResponse<StudyResponse> terminate(
            @Parameter(description = "스터디 ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID studyId);

    @Operation(summary = "전체 스터디 목록 조회", description = "승인된 모든 스터디 목록을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ApiResponse<List<StudyResponse>> getAllStudies();

    @Operation(summary = "스터디 목록 페이징 조회", description = "승인된 스터디 목록을 페이징하여 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ApiResponse<Page<StudyResponse>> getAllStudies(
            @Parameter(description = "페이징 정보 (page, size, sort)") Pageable pageable);

    @Operation(summary = "스터디 상세 조회", description = "특정 스터디의 상세 정보를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스터디를 찾을 수 없음")
    })
    ApiResponse<StudyResponse> getStudyById(
            @Parameter(description = "스터디 ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID studyId);
}
