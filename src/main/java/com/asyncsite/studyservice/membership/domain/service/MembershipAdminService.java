package com.asyncsite.studyservice.membership.domain.service;

import com.asyncsite.studyservice.membership.adapter.in.web.MembershipAdminControllerDocs.ApplicationAdminResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MembershipAdminService {

    public List<ApplicationAdminResponse> getPendingApplications(int days) {
        // Mock data for now
        return List.of(
                ApplicationAdminResponse.createMock("React 심화 스터디", 8),
                ApplicationAdminResponse.createMock("알고리즘 스터디", 10),
                ApplicationAdminResponse.createMock("Spring Boot 스터디", 15)
        );
    }

    public List<Map<String, Object>> getInactiveMembers(int days) {
        // Mock data for now
        return List.of(
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
    }

    public Map<String, Object> resetStudyMembers(UUID studyId) {
        // Mock data for now
        return Map.of(
                "studyId", studyId.toString(),
                "removedMembers", 12,
                "timestamp", LocalDateTime.now().toString()
        );
    }

    public Map<String, Object> getMemberChurnReport(int days) {
        // Mock data for now
        return Map.of(
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
    }

    public Map<String, Object> getMembershipStatistics() {
        // Mock data for now
        return Map.of(
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
    }
}