package com.asyncsite.studyservice.membership.adapter.in.web;

import com.asyncsite.coreplatform.common.dto.ApiResponse;
import com.asyncsite.studyservice.membership.adapter.in.web.dto.AcceptApplicationRequest;
import com.asyncsite.studyservice.membership.adapter.in.web.dto.ApplicationRequest;
import com.asyncsite.studyservice.membership.adapter.in.web.dto.ApplicationResponse;
import com.asyncsite.studyservice.membership.adapter.in.web.dto.RejectApplicationRequest;
import com.asyncsite.studyservice.membership.domain.model.Application;
import com.asyncsite.studyservice.membership.domain.model.Member;
import com.asyncsite.studyservice.membership.domain.port.in.ApplicationUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/studies/{studyId}/applications")
@Tag(name = "Application Management", description = "스터디 지원 관리 API")
@RequiredArgsConstructor
public class ApplicationController implements ApplicationControllerDocs {
    private final ApplicationUseCase applicationUseCase;

    @Override
    @PostMapping
    public ApiResponse<ApplicationResponse> apply(
            @PathVariable UUID studyId,
            @RequestBody ApplicationRequest request
    ) {
        Application application = applicationUseCase.apply(studyId, request.getApplicantId(), request.getAnswers());
        return ApiResponse.createdResponse(ApplicationResponse.from(application));
    }

    @Override
    @GetMapping
    public ApiResponse<Page<ApplicationResponse>> getApplications(
            @PathVariable UUID studyId,
            Pageable pageable) {
        Page<Application> applicationPage = applicationUseCase.getApplications(studyId, pageable);
        Page<ApplicationResponse> responsePage = applicationPage.map(ApplicationResponse::from);
        return ApiResponse.success(responsePage);
    }

    @Override
    @GetMapping("/{applicationId}")
    public ApiResponse<ApplicationResponse> getApplication(
            @PathVariable UUID studyId,
            @PathVariable UUID applicationId) {
        Application application = applicationUseCase.getApplicationById(applicationId);
        return ApiResponse.success(ApplicationResponse.from(application));
    }

    @Override
    @PostMapping("/{applicationId}/accept")
    public ApiResponse<Map<String, Object>> acceptApplication(
            @PathVariable UUID studyId,
            @PathVariable UUID applicationId,
            @Valid @RequestBody AcceptApplicationRequest request) {

        Member member = applicationUseCase.accept(applicationId, request.getReviewerId(), request.getNote());
        UUID memberId = member.getId();
        
        Map<String, Object> response = Map.of(
                "memberId", memberId.toString(),
                "studyId", studyId.toString(),
                "applicationId", applicationId.toString(),
                "acceptedAt", LocalDateTime.now().toString()
        );
        return ApiResponse.success(response);
    }

    @Override
    @PostMapping("/{applicationId}/reject")
    public ApiResponse<Void> rejectApplication(
            @PathVariable UUID studyId,
            @PathVariable UUID applicationId,
            @Valid @RequestBody RejectApplicationRequest request) {
        applicationUseCase.reject(applicationId, request.getReviewerId(), request.getReason());
        return ApiResponse.success(null);
    }

    @Override
    @DeleteMapping("/{applicationId}")
    public ApiResponse<Void> cancelApplication(
            @PathVariable UUID studyId,
            @PathVariable UUID applicationId,
            @RequestParam String applicantId) {
        applicationUseCase.cancelApplication(applicationId, applicantId);
        return ApiResponse.success(null);
    }
}