package com.asyncsite.studyservice.membership.adapter.in.web;

import com.asyncsite.coreplatform.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1/admin/membership")
@Tag(name = "Membership Admin", description = "멤버십 관리자 API")
@Slf4j
public class MembershipAdminWebAdapter {
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplicationAdminResponse {
        private UUID applicationId;
        private UUID studyId;
        private String studyTitle;
        private String applicantId;
        private String applicantName;
        private String status;
        private LocalDateTime appliedAt;
        private int pendingDays;
        
        public static ApplicationAdminResponse createMock(String studyTitle, int pendingDays) {
            return ApplicationAdminResponse.builder()
                    .applicationId(UUID.randomUUID())
                    .studyId(UUID.randomUUID())
                    .studyTitle(studyTitle)
                    .applicantId("user" + new Random().nextInt(1000))
                    .applicantName("사용자" + new Random().nextInt(1000))
                    .status("PENDING")
                    .appliedAt(LocalDateTime.now().minusDays(pendingDays))
                    .pendingDays(pendingDays)
                    .build();
        }
    }
    
    @GetMapping("/applications/pending")
    @Operation(summary = "장기 미처리 지원", description = "일정 기간 이상 처리되지 않은 지원서를 조회합니다")
    public ApiResponse<List<ApplicationAdminResponse>> getPendingApplications(
            @RequestParam(defaultValue = "7") int days) {
        
        log.info("Mock API: 장기 미처리 지원 조회 - days: {}", days);
        
        List<ApplicationAdminResponse> applications = List.of(
                ApplicationAdminResponse.createMock("React 심화 스터디", 8),
                ApplicationAdminResponse.createMock("알고리즘 스터디", 10),
                ApplicationAdminResponse.createMock("Spring Boot 스터디", 15)
        );
        
        return ApiResponse.success(applications);
    }
    
    @GetMapping("/members/inactive")
    @Operation(summary = "비활성 멤버 조회", description = "비활성 상태의 멤버를 조회합니다")
    public ApiResponse<List<Map<String, Object>>> getInactiveMembers(
            @RequestParam(defaultValue = "30") int days) {
        
        log.info("Mock API: 비활성 멤버 조회 - days: {}", days);
        
        List<Map<String, Object>> members = List.of(
                Map.of(
                        "memberId", UUID.randomUUID().toString(),
                        "userId", "user123",
                        "studyTitle", "Python 기초",
                        "lastActivityDate", LocalDateTime.now().minusDays(45).toString(),
                        "inactiveDays", 45,
                        "attendanceRate", 20
                ),
                Map.of(
                        "memberId", UUID.randomUUID().toString(),
                        "userId", "user456",
                        "studyTitle", "자바스크립트 심화",
                        "lastActivityDate", LocalDateTime.now().minusDays(35).toString(),
                        "inactiveDays", 35,
                        "attendanceRate", 15
                )
        );
        
        return ApiResponse.success(members);
    }
    
    @PostMapping("/studies/{studyId}/members/reset")
    @Operation(summary = "멤버 초기화", description = "스터디의 모든 멤버를 초기화합니다")
    public ApiResponse<Map<String, Object>> resetStudyMembers(@PathVariable UUID studyId) {
        
        log.info("Mock API: 멤버 초기화 - studyId: {}", studyId);
        
        Map<String, Object> result = Map.of(
                "studyId", studyId.toString(),
                "removedMembers", 12,
                "timestamp", LocalDateTime.now().toString()
        );
        
        return ApiResponse.success(result);
    }
    
    @GetMapping("/reports/member-churn")
    @Operation(summary = "멤버 이탈률 리포트", description = "멤버 이탈률 분석 리포트를 조회합니다")
    public ApiResponse<Map<String, Object>> getMemberChurnReport(
            @RequestParam(defaultValue = "30") int days) {
        
        log.info("Mock API: 멤버 이탈률 리포트 - days: {}", days);
        
        Map<String, Object> report = Map.of(
                "period", Map.of("from", LocalDateTime.now().minusDays(days).toString(), "to", LocalDateTime.now().toString()),
                "totalChurn", 45,
                "churnRate", 12.5,
                "churnByReason", Map.of(
                        "INACTIVE", 20,
                        "VOLUNTARY", 15,
                        "REMOVED", 10
                ),
                "churnByStudyCategory", Map.of(
                        "PROGRAMMING", 25,
                        "LANGUAGE", 10,
                        "DESIGN", 10
                ),
                "averageStayDuration", 45.5,
                "riskGroups", List.of(
                        Map.of("studyTitle", "하드코어 알고리즘", "churnRate", 35.0, "memberCount", 20),
                        Map.of("studyTitle", "초고속 웹개발", "churnRate", 28.5, "memberCount", 15)
                )
        );
        
        return ApiResponse.success(report);
    }
    
    @GetMapping("/statistics/overview")
    @Operation(summary = "멤버십 통계 개요", description = "전체 멤버십 통계를 조회합니다")
    public ApiResponse<Map<String, Object>> getMembershipStatistics() {
        
        log.info("Mock API: 멤버십 통계 개요");
        
        Map<String, Object> statistics = Map.of(
                "totalApplications", Map.of(
                        "count", 1250,
                        "pending", 45,
                        "accepted", 980,
                        "rejected", 225
                ),
                "totalMembers", Map.of(
                        "active", 850,
                        "inactive", 130,
                        "total", 980
                ),
                "applicationTrends", Map.of(
                        "daily", 15,
                        "weekly", 105,
                        "monthly", 420
                ),
                "conversionRate", Map.of(
                        "applicationToMember", 78.4,
                        "memberRetention", 86.7
                ),
                "topStudies", List.of(
                        Map.of("title", "React 마스터", "memberCount", 45, "applicationCount", 120),
                        Map.of("title", "알고리즘 정복", "memberCount", 38, "applicationCount", 95),
                        Map.of("title", "Spring Boot 실전", "memberCount", 32, "applicationCount", 78)
                )
        );
        
        return ApiResponse.success(statistics);
    }
}