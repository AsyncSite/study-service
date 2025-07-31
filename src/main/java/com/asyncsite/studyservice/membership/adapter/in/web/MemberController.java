package com.asyncsite.studyservice.membership.adapter.in.web;

import com.asyncsite.coreplatform.common.dto.ApiResponse;
import com.asyncsite.studyservice.membership.adapter.in.web.dto.*;
import com.asyncsite.studyservice.membership.domain.model.Member;
import com.asyncsite.studyservice.membership.domain.port.in.ManageMembersUseCase;
import com.asyncsite.studyservice.membership.domain.port.in.QueryMembersUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1/studies/{studyId}/members")
@Tag(name = "Member Management", description = "스터디 멤버 관리 API")
@RequiredArgsConstructor
@Slf4j
public class MemberWebAdapter {
    
    private final ManageMembersUseCase manageMembersUseCase;
    private final QueryMembersUseCase queryMembersUseCase;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberResponse {
        private UUID id;
        private UUID studyId;
        private String userId;
        private String role;
        private LocalDateTime joinedAt;
        private LocalDateTime leftAt;
        private boolean isActive;
        private int warningCount;
        
        public static MemberResponse createMock(UUID studyId, String userId, String role) {
            return MemberResponse.builder()
                    .id(UUID.randomUUID())
                    .studyId(studyId)
                    .userId(userId)
                    .role(role)
                    .joinedAt(LocalDateTime.now().minusDays(30))
                    .isActive(true)
                    .warningCount(0)
                    .build();
        }
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangeMemberRoleRequest {
        private String requesterId;
        private String newRole;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WarnMemberRequest {
        private String requesterId;
        private String reason;
    }
    
    @GetMapping
    @Operation(summary = "멤버 목록 조회", description = "스터디 멤버 목록을 조회합니다")
    public ApiResponse<Page<MemberResponse>> getMembers(
            @PathVariable UUID studyId,
            Pageable pageable) {
        
        log.info("Mock API: 멤버 목록 조회 - studyId: {}", studyId);
        
        List<MemberResponse> members = List.of(
                MemberResponse.createMock(studyId, "leader123", "LEADER"),
                MemberResponse.createMock(studyId, "member1", "MEMBER"),
                MemberResponse.createMock(studyId, "member2", "MEMBER"),
                MemberResponse.createMock(studyId, "member3", "CO_LEADER"),
                MemberResponse.createMock(studyId, "member4", "MEMBER")
        );
        
        Page<MemberResponse> page = new PageImpl<>(members, pageable, members.size());
        return ApiResponse.success(page);
    }
    
    @GetMapping("/{memberId}")
    @Operation(summary = "멤버 상세 조회", description = "멤버 상세 정보를 조회합니다")
    public ApiResponse<MemberResponse> getMember(
            @PathVariable UUID studyId,
            @PathVariable UUID memberId) {
        
        log.info("Mock API: 멤버 상세 조회 - memberId: {}", memberId);
        
        MemberResponse response = MemberResponse.builder()
                .id(memberId)
                .studyId(studyId)
                .userId("member123")
                .role("MEMBER")
                .joinedAt(LocalDateTime.now().minusDays(15))
                .isActive(true)
                .warningCount(1)
                .build();
        
        return ApiResponse.success(response);
    }
    
    @GetMapping("/count")
    @Operation(summary = "멤버 수 조회", description = "스터디의 총 멤버 수를 조회합니다")
    public ApiResponse<Integer> getMemberCount(@PathVariable UUID studyId) {
        
        log.info("Mock API: 멤버 수 조회 - studyId: {}", studyId);
        
        return ApiResponse.success(5);
    }
    
    @PutMapping("/{memberId}/role")
    @Operation(summary = "멤버 역할 변경", description = "멤버의 역할을 변경합니다 (리더 전용)")
    public ApiResponse<Void> changeMemberRole(
            @PathVariable UUID studyId,
            @PathVariable UUID memberId,
            @RequestBody ChangeMemberRoleRequest request) {
        
        log.info("Mock API: 멤버 역할 변경 - memberId: {}, newRole: {}", memberId, request.getNewRole());
        
        return ApiResponse.success(null);
    }
    
    @DeleteMapping("/{memberId}")
    @Operation(summary = "멤버 제명", description = "멤버를 스터디에서 제명합니다")
    public ApiResponse<Void> removeMember(
            @PathVariable UUID studyId,
            @PathVariable UUID memberId,
            @RequestParam String requesterId) {
        
        log.info("Mock API: 멤버 제명 - memberId: {}, requesterId: {}", memberId, requesterId);
        
        return ApiResponse.success(null);
    }
    
    @PostMapping("/{memberId}/warnings")
    @Operation(summary = "멤버 경고", description = "멤버에게 경고를 발송합니다")
    public ApiResponse<Void> warnMember(
            @PathVariable UUID studyId,
            @PathVariable UUID memberId,
            @RequestBody WarnMemberRequest request) {
        
        log.info("Mock API: 멤버 경고 - memberId: {}, reason: {}", memberId, request.getReason());
        
        return ApiResponse.success(null);
    }
    
    @PostMapping("/leave")
    @Operation(summary = "스터디 탈퇴", description = "스터디에서 자발적으로 탈퇴합니다")
    public ApiResponse<Void> leaveStudy(
            @PathVariable UUID studyId,
            @RequestParam String userId) {
        
        log.info("Mock API: 스터디 탈퇴 - studyId: {}, userId: {}", studyId, userId);
        
        return ApiResponse.success(null);
    }
    
    @GetMapping("/statistics")
    @Operation(summary = "멤버 통계", description = "스터디 멤버 통계를 조회합니다")
    public ApiResponse<Map<String, Object>> getMemberStatistics(@PathVariable UUID studyId) {
        
        log.info("Mock API: 멤버 통계 조회 - studyId: {}", studyId);
        
        Map<String, Object> statistics = Map.of(
                "totalMembers", 5,
                "activeMembers", 4,
                "averageAttendanceRate", 85.5,
                "warningCount", Map.of(
                        "0", 3,
                        "1", 1,
                        "2", 1
                ),
                "roleDistribution", Map.of(
                        "LEADER", 1,
                        "CO_LEADER", 1,
                        "MEMBER", 3
                )
        );
        
        return ApiResponse.success(statistics);
    }
}