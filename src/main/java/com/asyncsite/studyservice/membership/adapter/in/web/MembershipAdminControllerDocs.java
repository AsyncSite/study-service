package com.asyncsite.studyservice.membership.adapter.in.web;

import com.asyncsite.coreplatform.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Tag(name = "Membership Admin", description = "멤버십 관리자 API")
public interface MembershipAdminControllerDocs {

    @Operation(summary = "장기 미처리 지원", description = "일정 기간 이상 처리되지 않은 지원서를 조회합니다")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ApiResponse<List<ApplicationAdminResponse>> getPendingApplications(
            @Parameter(description = "미처리 기간 (일)") @RequestParam int days);

    @Operation(summary = "비활성 멤버 조회", description = "비활성 상태의 멤버를 조회합니다")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ApiResponse<List<Map<String, Object>>> getInactiveMembers(
            @Parameter(description = "비활성 기간 (일)") @RequestParam int days);

    @Operation(summary = "멤버 초기화", description = "스터디의 모든 멤버를 초기화합니다")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "초기화 성공")
    })
    ApiResponse<Map<String, Object>> resetStudyMembers(
            @Parameter(description = "스터디 ID") @PathVariable UUID studyId);

    @Operation(summary = "멤버 이탈률 리포트", description = "멤버 이탈률 분석 리포트를 조회합니다")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ApiResponse<Map<String, Object>> getMemberChurnReport(
            @Parameter(description = "조회 기간 (일)") @RequestParam int days);

    @Operation(summary = "멤버십 통계 개요", description = "전체 멤버십 통계를 조회합니다")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ApiResponse<Map<String, Object>> getMembershipStatistics();

    // Inner class for ApplicationAdminResponse
    class ApplicationAdminResponse {
        private UUID applicationId;
        private UUID studyId;
        private String studyTitle;
        private String applicantId;
        private String applicantName;
        private String status;
        private java.time.LocalDateTime appliedAt;
        private int pendingDays;

        public ApplicationAdminResponse(UUID applicationId, UUID studyId, String studyTitle, String applicantId, String applicantName, String status, java.time.LocalDateTime appliedAt, int pendingDays) {
            this.applicationId = applicationId;
            this.studyId = studyId;
            this.studyTitle = studyTitle;
            this.applicantId = applicantId;
            this.applicantName = applicantName;
            this.status = status;
            this.appliedAt = appliedAt;
            this.pendingDays = pendingDays;
        }

        public static ApplicationAdminResponse createMock(String studyTitle, int pendingDays) {
            return new ApplicationAdminResponse(
                    UUID.randomUUID(),
                    UUID.randomUUID(),
                    studyTitle,
                    "user" + new java.util.Random().nextInt(1000),
                    "사용자" + new java.util.Random().nextInt(1000),
                    "PENDING",
                    java.time.LocalDateTime.now().minusDays(pendingDays),
                    pendingDays
            );
        }
    }
}