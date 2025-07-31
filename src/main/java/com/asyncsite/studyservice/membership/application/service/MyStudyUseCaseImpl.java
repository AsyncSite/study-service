package com.asyncsite.studyservice.membership.application.service;

import com.asyncsite.studyservice.membership.domain.model.Application;
import com.asyncsite.studyservice.membership.domain.model.Member;
import com.asyncsite.studyservice.membership.domain.port.in.MyStudyUseCase;
import com.asyncsite.studyservice.membership.domain.service.MyStudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MyStudyUseCaseImpl implements MyStudyUseCase {
    private final MyStudyService myStudyService;

    @Override
    public List<Application> getMyApplications(String userId) {
        return myStudyService.getMyApplications(userId);
    }

    @Override
    public List<Member> getMyStudies(String userId) {
        return myStudyService.getMyStudies(userId);
    }

    @Override
    public Map<String, Object> getMyDashboard(String userId) {
        return myStudyService.getMyDashboard(userId);
    }

    @Override
    public List<Map<String, Object>> getRecommendations(String userId) {
        return myStudyService.getRecommendations(userId);
    }
}