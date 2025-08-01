package com.asyncsite.studyservice.membership.adapter.in.web;

import com.asyncsite.coreplatform.common.dto.ApiResponse;
import com.asyncsite.studyservice.membership.adapter.in.web.dto.AcceptApplicationRequest;
import com.asyncsite.studyservice.membership.adapter.in.web.dto.ApplicationRequest;
import com.asyncsite.studyservice.membership.adapter.in.web.dto.ApplicationResponse;
import com.asyncsite.studyservice.membership.adapter.in.web.dto.RejectApplicationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.UUID;

@Tag(name = "Application Management", description = "스터디 지원 관리 API")
public interface ApplicationControllerDocs {

    @Operation(summary = "스터디 지원", description = "스터디에 지원합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "스터디 지원 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 지원한 스터디")
    })
    ApiResponse<ApplicationResponse> apply(
            @Parameter(description = "스터디 ID") @PathVariable UUID studyId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "지원 정보(지원자 ID, 질의응답)") ApplicationRequest request
    );

    @Operation(summary = "지원서 목록 조회", description = "스터디의 지원서 목록을 조회합니다. (스터디장만 가능)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "조회 권한 없음")
    })
    ApiResponse<Page<ApplicationResponse>> getApplications(
            @Parameter(description = "스터디 ID") @PathVariable UUID studyId,
            @Parameter(hidden = true) Pageable pageable
    );

    @Operation(summary = "지원서 상세 조회", description = "지원서 상세 정보를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "지원서를 찾을 수 없음")
    })
    ApiResponse<ApplicationResponse> getApplication(
            @Parameter(description = "스터디 ID") @PathVariable UUID studyId,
            @Parameter(description = "지원서 ID") @PathVariable UUID applicationId
    );

    @Operation(summary = "지원 승인", description = "지원을 승인하고 멤버로 등록합니다. (스터디장만 가능)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "지원 승인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "승인 권한 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "지원서를 찾을 수 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 처리된 지원서")
    })
    ApiResponse<Map<String, Object>> acceptApplication(
            @Parameter(description = "스터디 ID") @PathVariable UUID studyId,
            @Parameter(description = "지원서 ID") @PathVariable UUID applicationId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "지원 승인 정보")
            @Valid AcceptApplicationRequest request
    );

    @Operation(summary = "지원 거절", description = "지원을 거절합니다. (스터디장만 가능)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "지원 거절 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "거절 권한 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "지원서를 찾을 수 없음")
    })
    ApiResponse<Void> rejectApplication(
            @Parameter(description = "스터디 ID") @PathVariable UUID studyId,
            @Parameter(description = "지원서 ID") @PathVariable UUID applicationId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "지원 거절 정보")
            @Valid RejectApplicationRequest request
    );

    @Operation(summary = "지원 취소", description = "본인의 지원을 취소합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "지원 취소 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "본인의 지원서만 취소 가능"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "지원서를 찾을 수 없음")
    })
    ApiResponse<Void> cancelApplication(
            @Parameter(description = "스터디 ID") @PathVariable UUID studyId,
            @Parameter(description = "지원서 ID") @PathVariable UUID applicationId,
            @Parameter(description = "취소 요청자 ID") @RequestParam String applicantId
    );
}