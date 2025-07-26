package com.asyncsite.studyservice.membership.application.service;

import com.asyncsite.studyservice.membership.domain.model.Application;
import com.asyncsite.studyservice.membership.domain.port.in.GetApplicationsUseCase;
import com.asyncsite.studyservice.membership.domain.port.out.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetApplicationsService implements GetApplicationsUseCase {
    private final ApplicationRepository applicationRepository;
    
    @Override
    public List<Application> getApplicationsByStudyId(UUID studyId) {
        return applicationRepository.findByStudyId(studyId);
    }
    
    @Override
    public Page<Application> getApplicationsByStudyId(UUID studyId, Pageable pageable) {
        return applicationRepository.findByStudyId(studyId, pageable);
    }
    
    @Override
    public Optional<Application> getApplicationById(UUID applicationId) {
        return applicationRepository.findById(applicationId);
    }
    
    @Override
    public List<Application> getApplicationsByApplicantId(String applicantId) {
        return applicationRepository.findByApplicantId(applicantId);
    }
}