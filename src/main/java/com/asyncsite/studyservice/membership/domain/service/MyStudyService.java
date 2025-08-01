package com.asyncsite.studyservice.membership.domain.service;

import com.asyncsite.studyservice.membership.domain.model.Application;
import com.asyncsite.studyservice.membership.domain.model.Member;
import com.asyncsite.studyservice.membership.domain.port.out.ApplicationRepository;
import com.asyncsite.studyservice.membership.domain.port.out.MemberRepository;
import com.asyncsite.studyservice.study.domain.port.out.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyStudyService {
    private final ApplicationRepository applicationRepository;
    private final MemberRepository memberRepository;

    public List<Application> getMyApplications(String userId) {
        List<Application> applications = applicationRepository.findByApplicantId(userId);
        return applications;
    }

    public List<Member> getMyStudies(String userId) {
        List<Member> members = memberRepository.findByUserId(userId);
        return members;
    }

    public Map<String, Object> getMyDashboard(String userId) {
        List<Member> myStudies = memberRepository.findByUserId(userId);
        long totalStudies = myStudies.size();
        long leadingStudies = myStudies.stream().filter(Member::isLeader).count();

        // Placeholder for actual averageAttendanceRate and totalWarnings logic
        double averageAttendanceRate = 0.0;
        long totalWarnings = 0;

        return Map.of(
                "summary", Map.of(
                        "totalStudies", totalStudies,
                        "leadingStudies", leadingStudies,
                        "averageAttendanceRate", averageAttendanceRate,
                        "totalWarnings", totalWarnings
                ),
                "recentActivities", List.of(),
                "upcomingSchedules", List.of()
        );
    }

    public List<Map<String, Object>> getRecommendations(String userId) {
        // Implement actual logic using repositories and potentially external services
        return List.of();
    }
}