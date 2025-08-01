package com.asyncsite.studyservice.membership.adapter.in.web;

import com.asyncsite.coreplatform.common.dto.ApiResponse;
import com.asyncsite.studyservice.membership.adapter.in.web.dto.ApplicationFormRequest;
import com.asyncsite.studyservice.membership.adapter.in.web.dto.ApplicationFormResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.UUID;

@Tag(name = "Application Form Management", description = "지원서 양식 관리 API")
public interface ApplicationFormControllerDocs {

    @Operation(summary = "지원서 양식 조회", description = "스터디의 활성 지원서 양식을 조회합니다")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "활성화된 지원서 양식이 없음")
    })
    ApiResponse<ApplicationFormResponse> getApplicationForm(
            @Parameter(description = "스터디 ID") @PathVariable UUID studyId);

    @Operation(summary = "지원서 양식 생성", description = "새로운 지원서 양식을 생성합니다")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "지원서 양식 생성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    ApiResponse<ApplicationFormResponse> createApplicationForm(
            @Parameter(description = "스터디 ID") @PathVariable UUID studyId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "지원서 양식 정보") @Valid @RequestBody ApplicationFormRequest request);

    @Operation(summary = "지원서 양식 수정", description = "지원서 양식을 수정합니다")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "지원서 양식 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "지원서 양식을 찾을 수 없음")
    })
    ApiResponse<ApplicationFormResponse> updateApplicationForm(
            @Parameter(description = "스터디 ID") @PathVariable UUID studyId,
            @Parameter(description = "양식 ID") @PathVariable UUID formId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "지원서 양식 정보") @Valid @RequestBody ApplicationFormRequest request);

    @Operation(summary = "지원서 양식 비활성화", description = "지원서 양식을 비활성화합니다")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "지원서 양식 비활성화 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "지원서 양식을 찾을 수 없음")
    })
    ApiResponse<Void> deactivateApplicationForm(
            @Parameter(description = "스터디 ID") @PathVariable UUID studyId,
            @Parameter(description = "양식 ID") @PathVariable UUID formId);

    @Operation(summary = "기본 템플릿 조회", description = "지원서 양식 기본 템플릿을 조회합니다")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ApiResponse<Map<String, Object>> getApplicationFormTemplate();
}