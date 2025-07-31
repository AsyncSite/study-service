package com.asyncsite.studyservice.membership.application.service;

import com.asyncsite.studyservice.membership.adapter.in.web.MembershipAdminControllerDocs.ApplicationAdminResponse;
import com.asyncsite.studyservice.membership.domain.port.in.MembershipAdminUseCase;
import com.asyncsite.studyservice.membership.domain.service.MembershipAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MembershipAdminUseCaseImpl implements MembershipAdminUseCase {
    private final MembershipAdminService membershipAdminService;

    @Override
    public List<ApplicationAdminResponse> getPendingApplications(int days) {
        return membershipAdminService.getPendingApplications(days);
    }

    @Override
    public List<Map<String, Object>> getInactiveMembers(int days) {
        return membershipAdminService.getInactiveMembers(days);
    }

    @Override
    public Map<String, Object> resetStudyMembers(UUID studyId) {
        return membershipAdminService.resetStudyMembers(studyId);
    }

    @Override
    public Map<String, Object> getMemberChurnReport(int days) {
        return membershipAdminService.getMemberChurnReport(days);
    }

    @Override
    public Map<String, Object> getMembershipStatistics() {
        return membershipAdminService.getMembershipStatistics();
    }
}