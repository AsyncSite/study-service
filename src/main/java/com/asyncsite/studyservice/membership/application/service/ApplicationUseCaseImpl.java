package com.asyncsite.studyservice.membership.application.service;

import com.asyncsite.studyservice.membership.domain.model.Application;
import com.asyncsite.studyservice.membership.domain.model.Member;
import com.asyncsite.studyservice.membership.domain.port.in.ApplicationUseCase;
import com.asyncsite.studyservice.membership.domain.service.ApplicationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplicationUseCaseImpl implements ApplicationUseCase {
    private final ApplicationService applicationService;

    @Override
    public Application apply(UUID studyId, String applicantId, Map<String, String> answers) {
        return applicationService.apply(studyId, applicantId, answers);
    }

    @Override
    public Page<Application> getApplications(UUID studyId, Pageable pageable) {
        return applicationService.getApplications(studyId, pageable);
    }

    @Override
    public Application getApplicationById(UUID applicationId) {
        return applicationService.getApplicationById(applicationId);
    }

    @Override
    public Member accept(UUID applicationId, String reviewerId, String note) {
        return applicationService.accept(applicationId, reviewerId, note);
    }

    @Override
    public void reject(UUID applicationId, String reviewerId, String reason) {
        applicationService.reject(applicationId, reviewerId, reason);
    }

    @Override
    public void cancelApplication(UUID applicationId, String applicantId) {
        applicationService.cancelApplication(applicationId, applicantId);
    }
}
