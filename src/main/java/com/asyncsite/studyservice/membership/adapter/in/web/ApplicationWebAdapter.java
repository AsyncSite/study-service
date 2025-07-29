package com.asyncsite.studyservice.membership.adapter.in.web;

import com.asyncsite.coreplatform.common.dto.ApiResponse;
import com.asyncsite.studyservice.membership.adapter.in.web.dto.AcceptApplicationRequest;
import com.asyncsite.studyservice.membership.adapter.in.web.dto.ApplicationRequest;
import com.asyncsite.studyservice.membership.adapter.in.web.dto.ApplicationResponse;
import com.asyncsite.studyservice.membership.adapter.in.web.dto.RejectApplicationRequest;
import com.asyncsite.studyservice.membership.domain.model.Application;
import com.asyncsite.studyservice.membership.domain.model.Member;
import com.asyncsite.studyservice.membership.domain.port.in.AcceptApplicationUseCase;
import com.asyncsite.studyservice.membership.domain.port.in.ApplyToStudyUseCase;
import com.asyncsite.studyservice.membership.domain.port.in.QueryApplicationUseCase;
import com.asyncsite.studyservice.membership.domain.port.in.RejectApplicationUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/studies/{studyId}/applications")
@Tag(name = "Application Management", description = "스터디 지원 관리 API")
@RequiredArgsConstructor
public class ApplicationWebAdapter implements ApplicationControllerDocs {
    private final ApplyToStudyUseCase applyToStudyUseCase;
    private final QueryApplicationUseCase queryApplicationUseCase;
    private final AcceptApplicationUseCase acceptApplicationUseCase;
    private final RejectApplicationUseCase rejectApplicationUseCase;

    @Override
    @PostMapping
    public ApiResponse<ApplicationResponse> apply(
            @PathVariable UUID studyId,
            @RequestBody ApplicationRequest request
    ) {
        Application application = applyToStudyUseCase.apply(studyId, request.getApplicantId(), request.getAnswers());
        return ApiResponse.createdResponse(ApplicationResponse.from(application));
    }

    @Override
    @GetMapping
    public ApiResponse<Page<ApplicationResponse>> getApplications(
            @PathVariable UUID studyId,
            Pageable pageable) {
        List<ApplicationResponse> applications = List.of(
                ApplicationResponse.createMock(studyId, "user1"),
                ApplicationResponse.createMock(studyId, "user2"),
                ApplicationResponse.createMock(studyId, "user3")
        );
        
        Page<ApplicationResponse> page = new PageImpl<>(applications, pageable, applications.size());
        return ApiResponse.success(page);
    }

    @Override
    @GetMapping("/{applicationId}")
    public ApiResponse<ApplicationResponse> getApplication(
            @PathVariable UUID studyId,
            @PathVariable UUID applicationId) {
        Optional<Application> application = queryApplicationUseCase.getApplicationById(applicationId);
        if (application.isEmpty()) {
            return ApiResponse.<ApplicationResponse>error("APPLICATION_NOT_FOUND", "지원서를 찾을 수 없습니다.", null);
        }
        
        return ApiResponse.success(ApplicationResponse.from(application.get()));
    }

    @Override
    @PostMapping("/{applicationId}/accept")
    public ApiResponse<Map<String, Object>> acceptApplication(
            @PathVariable UUID studyId,
            @PathVariable UUID applicationId,
            @Valid @RequestBody AcceptApplicationRequest request) {

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

    @Override
    @PostMapping("/{applicationId}/reject")
    public ApiResponse<Void> rejectApplication(
            @PathVariable UUID studyId,
            @PathVariable UUID applicationId,
            @Valid @RequestBody RejectApplicationRequest request) {
        rejectApplicationUseCase.reject(applicationId, request.getReviewerId(), request.getReason());
        return ApiResponse.success(null);
    }

    @Override
    @DeleteMapping("/{applicationId}")
    public ApiResponse<Void> cancelApplication(
            @PathVariable UUID studyId,
            @PathVariable UUID applicationId,
            @RequestParam String applicantId) {
        queryApplicationUseCase.cancelApplication(applicationId, applicantId);
        return ApiResponse.success(null);
    }
}