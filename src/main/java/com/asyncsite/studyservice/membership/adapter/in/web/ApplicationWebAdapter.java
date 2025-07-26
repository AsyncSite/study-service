package com.asyncsite.studyservice.membership.adapter.in.web;

import com.asyncsite.coreplatform.common.dto.ApiResponse;
import com.asyncsite.studyservice.membership.adapter.in.web.dto.*;
import com.asyncsite.studyservice.membership.domain.model.Application;
import com.asyncsite.studyservice.membership.domain.model.Member;
import com.asyncsite.studyservice.membership.domain.port.in.AcceptApplicationUseCase;
import com.asyncsite.studyservice.membership.domain.port.in.ApplyToStudyUseCase;
import com.asyncsite.studyservice.membership.domain.port.in.QueryApplicationUseCase;
import com.asyncsite.studyservice.membership.domain.port.in.RejectApplicationUseCase;
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
@RequestMapping("/api/v1/studies/{studyId}/applications")
@Tag(name = "Application Management", description = "스터디 지원 관리 API")
@RequiredArgsConstructor
@Slf4j
public class ApplicationWebAdapter {
    
    private final ApplyToStudyUseCase applyToStudyUseCase;
    private final QueryApplicationUseCase queryApplicationUseCase;
    private final AcceptApplicationUseCase acceptApplicationUseCase;
    private final RejectApplicationUseCase rejectApplicationUseCase;
    
    
    @PostMapping
    @Operation(summary = "스터디 지원", description = "스터디에 지원합니다")
    public ApiResponse<ApplicationResponse> apply(
            @PathVariable UUID studyId,
            @RequestBody ApplicationRequest request) {
        
        log.info("Mock API: 스터디 지원 - studyId: {}, applicantId: {}", studyId, request.getApplicantId());
        
        ApplicationResponse response = ApplicationResponse.createMock(studyId, request.getApplicantId());
        return ApiResponse.createdResponse(response);
    }
    
    @GetMapping
    @Operation(summary = "지원서 목록 조회", description = "스터디의 지원서 목록을 조회합니다")
    public ApiResponse<Page<ApplicationResponse>> getApplications(
            @PathVariable UUID studyId,
            Pageable pageable) {
        
        log.info("Mock API: 지원서 목록 조회 - studyId: {}", studyId);
        
        List<ApplicationResponse> applications = List.of(
                ApplicationResponse.createMock(studyId, "user1"),
                ApplicationResponse.createMock(studyId, "user2"),
                ApplicationResponse.createMock(studyId, "user3")
        );
        
        Page<ApplicationResponse> page = new PageImpl<>(applications, pageable, applications.size());
        return ApiResponse.success(page);
    }
    
    @GetMapping("/{applicationId}")
    @Operation(summary = "지원서 상세 조회", description = "지원서 상세 정보를 조회합니다")
    public ApiResponse<ApplicationResponse> getApplication(
            @PathVariable UUID studyId,
            @PathVariable UUID applicationId) {
        
        log.info("지원서 상세 조회 - applicationId: {}", applicationId);
        
        Optional<Application> application = queryApplicationUseCase.getApplicationById(applicationId);
        if (application.isEmpty()) {
            return ApiResponse.<ApplicationResponse>error("APPLICATION_NOT_FOUND", "지원서를 찾을 수 없습니다.", null);
        }
        
        return ApiResponse.success(ApplicationResponse.from(application.get()));
    }
    
    @PostMapping("/{applicationId}/accept")
    @Operation(summary = "지원 승인", description = "지원을 승인하고 멤버로 등록합니다")
    public ApiResponse<Map<String, Object>> acceptApplication(
            @PathVariable UUID studyId,
            @PathVariable UUID applicationId,
            @Valid @RequestBody AcceptApplicationRequest request) {
        
        log.info("지원 승인 - applicationId: {}, reviewerId: {}", applicationId, request.getReviewerId());
        
        Member member = acceptApplicationUseCase.accept(applicationId, request.getReviewerId(), request.getNote());
        UUID memberId = member.getId();
        
        Map<String, Object> response = Map.of(
                "memberId", memberId.toString(),
                "studyId", studyId.toString(),
                "applicationId", applicationId.toString(),
                "acceptedAt", LocalDateTime.now().toString()
        );
        
        return ApiResponse.success(response);
    }
    
    @PostMapping("/{applicationId}/reject")
    @Operation(summary = "지원 거절", description = "지원을 거절합니다")
    public ApiResponse<Void> rejectApplication(
            @PathVariable UUID studyId,
            @PathVariable UUID applicationId,
            @Valid @RequestBody RejectApplicationRequest request) {
        
        log.info("지원 거절 - applicationId: {}, reason: {}", applicationId, request.getReason());
        
        rejectApplicationUseCase.reject(applicationId, request.getReviewerId(), request.getReason());
        return ApiResponse.success(null);
    }
    
    @DeleteMapping("/{applicationId}")
    @Operation(summary = "지원 취소", description = "본인의 지원을 취소합니다")
    public ApiResponse<Void> cancelApplication(
            @PathVariable UUID studyId,
            @PathVariable UUID applicationId,
            @RequestParam String applicantId) {
        
        log.info("지원 취소 - applicationId: {}, applicantId: {}", applicationId, applicantId);
        
        queryApplicationUseCase.cancelApplication(applicationId, applicantId);
        return ApiResponse.success(null);
    }
}