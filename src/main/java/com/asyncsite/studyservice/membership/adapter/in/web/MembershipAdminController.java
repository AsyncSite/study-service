package com.asyncsite.studyservice.membership.adapter.in.web;

import com.asyncsite.coreplatform.common.dto.ApiResponse;
import com.asyncsite.studyservice.membership.domain.port.in.MembershipAdminUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/membership")
@Tag(name = "Membership Admin", description = "멤버십 관리자 API")
@RequiredArgsConstructor
public class MembershipAdminController implements MembershipAdminControllerDocs {

    private final MembershipAdminUseCase membershipAdminUseCase;

    @Override
    @GetMapping("/applications/pending")
    public ApiResponse<List<ApplicationAdminResponse>> getPendingApplications(
            @RequestParam int days) {
        return ApiResponse.success(membershipAdminUseCase.getPendingApplications(days));
    }

    @Override
    @GetMapping("/members/inactive")
    public ApiResponse<List<Map<String, Object>>> getInactiveMembers(
            @RequestParam int days) {
        return ApiResponse.success(membershipAdminUseCase.getInactiveMembers(days));
    }

    @Override
    @PostMapping("/studies/{studyId}/members/reset")
    public ApiResponse<Map<String, Object>> resetStudyMembers(@PathVariable UUID studyId) {
        return ApiResponse.success(membershipAdminUseCase.resetStudyMembers(studyId));
    }

    @Override
    @GetMapping("/reports/member-churn")
    public ApiResponse<Map<String, Object>> getMemberChurnReport(
            @RequestParam int days) {
        return ApiResponse.success(membershipAdminUseCase.getMemberChurnReport(days));
    }

    @Override
    @GetMapping("/statistics/overview")
    public ApiResponse<Map<String, Object>> getMembershipStatistics() {
        return ApiResponse.success(membershipAdminUseCase.getMembershipStatistics());
    }
}