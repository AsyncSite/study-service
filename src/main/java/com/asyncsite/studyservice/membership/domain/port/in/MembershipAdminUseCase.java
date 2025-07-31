package com.asyncsite.studyservice.membership.domain.port.in;

import com.asyncsite.studyservice.membership.adapter.in.web.MembershipAdminControllerDocs.ApplicationAdminResponse;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface MembershipAdminUseCase {
    List<ApplicationAdminResponse> getPendingApplications(int days);
    List<Map<String, Object>> getInactiveMembers(int days);
    Map<String, Object> resetStudyMembers(UUID studyId);
    Map<String, Object> getMemberChurnReport(int days);
    Map<String, Object> getMembershipStatistics();
}