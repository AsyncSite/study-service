package com.asyncsite.studyservice.membership.application.service;

import com.asyncsite.studyservice.membership.domain.model.Application;
import com.asyncsite.studyservice.membership.domain.port.in.QueryApplicationUseCase;
import com.asyncsite.studyservice.membership.domain.port.out.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryApplicationService implements QueryApplicationUseCase {
    private final ApplicationRepository applicationRepository;
    
    @Override
    public Page<Application> getApplicationsByStudyId(UUID studyId, Pageable pageable) {
        return applicationRepository.findByStudyId(studyId, pageable);
    }
    
    @Override
    public Optional<Application> getApplicationById(UUID applicationId) {
        return applicationRepository.findById(applicationId);
    }
    
    @Override
    @Transactional
    public void cancelApplication(UUID applicationId, String applicantId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found: " + applicationId));
        
        if (!application.getApplicantId().equals(applicantId)) {
            throw new IllegalArgumentException("Only the applicant can cancel their application");
        }
        
        application.cancel();
        applicationRepository.save(application);
    }
}