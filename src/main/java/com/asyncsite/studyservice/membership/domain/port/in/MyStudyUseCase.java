package com.asyncsite.studyservice.membership.domain.port.in;

import com.asyncsite.studyservice.membership.domain.model.Application;
import com.asyncsite.studyservice.membership.domain.model.Member;

import java.util.List;
import java.util.Map;

public interface MyStudyUseCase {
    List<Application> getMyApplications(String userId);
    List<Member> getMyStudies(String userId);
    Map<String, Object> getMyDashboard(String userId);
    List<Map<String, Object>> getRecommendations(String userId);
}