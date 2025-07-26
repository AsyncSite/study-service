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
@RequestMapping("/api/v1/my")
@Tag(name = "My Study", description = "내 스터디 관련 API")
@Slf4j
public class MyStudyWebAdapter {
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyApplicationResponse {
        private UUID applicationId;
        private UUID studyId;
        private String studyTitle;
        private String status;
        private LocalDateTime appliedAt;
        private String reviewNote;
        
        public static MyApplicationResponse createMock(String studyTitle, String status) {
            return MyApplicationResponse.builder()
                    .applicationId(UUID.randomUUID())
                    .studyId(UUID.randomUUID())
                    .studyTitle(studyTitle)
                    .status(status)
                    .appliedAt(LocalDateTime.now().minusDays(3))
                    .build();
        }
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyStudyResponse {
        private UUID memberId;
        private UUID studyId;
        private String studyTitle;
        private String role;
        private LocalDateTime joinedAt;
        private boolean isActive;
        private int attendanceRate;
        private int warningCount;
        
        public static MyStudyResponse createMock(String studyTitle, String role) {
            return MyStudyResponse.builder()
                    .memberId(UUID.randomUUID())
                    .studyId(UUID.randomUUID())
                    .studyTitle(studyTitle)
                    .role(role)
                    .joinedAt(LocalDateTime.now().minusDays(30))
                    .isActive(true)
                    .attendanceRate(85)
                    .warningCount(0)
                    .build();
        }
    }
    
    @GetMapping("/applications")
    @Operation(summary = "내 지원 현황", description = "내가 지원한 스터디 목록을 조회합니다")
    public ApiResponse<List<MyApplicationResponse>> getMyApplications(
            @RequestParam String userId) {
        
        log.info("Mock API: 내 지원 현황 조회 - userId: {}", userId);
        
        List<MyApplicationResponse> applications = List.of(
                MyApplicationResponse.createMock("React 심화 스터디", "PENDING"),
                MyApplicationResponse.createMock("알고리즘 문제풀이", "ACCEPTED"),
                MyApplicationResponse.createMock("Spring Boot 마스터", "REJECTED")
        );
        
        return ApiResponse.success(applications);
    }
    
    @GetMapping("/studies")
    @Operation(summary = "내가 참여중인 스터디", description = "내가 멤버로 참여중인 스터디 목록을 조회합니다")
    public ApiResponse<List<MyStudyResponse>> getMyStudies(
            @RequestParam String userId) {
        
        log.info("Mock API: 내가 참여중인 스터디 조회 - userId: {}", userId);
        
        List<MyStudyResponse> studies = List.of(
                MyStudyResponse.createMock("알고리즘 문제풀이", "LEADER"),
                MyStudyResponse.createMock("React 기초반", "MEMBER"),
                MyStudyResponse.createMock("데이터베이스 스터디", "CO_LEADER")
        );
        
        return ApiResponse.success(studies);
    }
    
    @GetMapping("/dashboard")
    @Operation(summary = "내 스터디 대시보드", description = "내 스터디 활동 요약 정보를 조회합니다")
    public ApiResponse<Map<String, Object>> getMyDashboard(
            @RequestParam String userId) {
        
        log.info("Mock API: 내 스터디 대시보드 조회 - userId: {}", userId);
        
        Map<String, Object> dashboard = Map.of(
                "summary", Map.of(
                        "totalStudies", 3,
                        "leadingStudies", 1,
                        "averageAttendanceRate", 87.5,
                        "totalWarnings", 0
                ),
                "recentActivities", List.of(
                        Map.of("type", "ATTENDANCE", "studyTitle", "알고리즘 문제풀이", "date", LocalDateTime.now().minusDays(1).toString()),
                        Map.of("type", "ASSIGNMENT_SUBMIT", "studyTitle", "React 기초반", "date", LocalDateTime.now().minusDays(2).toString()),
                        Map.of("type", "JOINED", "studyTitle", "데이터베이스 스터디", "date", LocalDateTime.now().minusDays(7).toString())
                ),
                "upcomingSchedules", List.of(
                        Map.of("studyTitle", "알고리즘 문제풀이", "date", LocalDateTime.now().plusDays(2).toString(), "location", "온라인"),
                        Map.of("studyTitle", "React 기초반", "date", LocalDateTime.now().plusDays(4).toString(), "location", "강남역 스터디룸")
                )
        );
        
        return ApiResponse.success(dashboard);
    }
    
    @GetMapping("/recommendations")
    @Operation(summary = "추천 스터디", description = "사용자에게 추천하는 스터디 목록을 조회합니다")
    public ApiResponse<List<Map<String, Object>>> getRecommendations(
            @RequestParam String userId) {
        
        log.info("Mock API: 추천 스터디 조회 - userId: {}", userId);
        
        List<Map<String, Object>> recommendations = List.of(
                Map.of(
                        "studyId", UUID.randomUUID().toString(),
                        "title", "TypeScript 심화",
                        "reason", "React 기초반 참여 이력 기반 추천",
                        "matchScore", 92,
                        "memberCount", 8
                ),
                Map.of(
                        "studyId", UUID.randomUUID().toString(),
                        "title", "시스템 디자인 스터디",
                        "reason", "비슷한 경력의 사용자들이 많이 참여",
                        "matchScore", 85,
                        "memberCount", 12
                ),
                Map.of(
                        "studyId", UUID.randomUUID().toString(),
                        "title", "코딩테스트 대비반",
                        "reason", "알고리즘 스터디 참여자 선호",
                        "matchScore", 78,
                        "memberCount", 15
                )
        );
        
        return ApiResponse.success(recommendations);
    }
}