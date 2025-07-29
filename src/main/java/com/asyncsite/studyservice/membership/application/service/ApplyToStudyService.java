package com.asyncsite.studyservice.membership.application.service;

import com.asyncsite.studyservice.membership.domain.exception.DuplicateApplicationException;
import com.asyncsite.studyservice.membership.domain.model.Application;
import com.asyncsite.studyservice.membership.domain.port.in.ApplyToStudyUseCase;
import com.asyncsite.studyservice.membership.domain.port.out.ApplicationRepository;
import com.asyncsite.studyservice.membership.domain.port.out.MembershipNotificationPort;
import com.asyncsite.studyservice.study.domain.port.out.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplyToStudyService implements ApplyToStudyUseCase {
    private final ApplicationRepository applicationRepository;
    private final StudyRepository studyRepository;
    private final MembershipNotificationPort notificationPort;
    
    @Override
    public Application apply(UUID studyId, String applicantId, Map<String, String> answers) {
        if (!studyRepository.isStudyExists(studyId)) throw new IllegalArgumentException("Study not found: " + studyId);
        if (!studyRepository.isStudyRecruiting(studyId)) throw new IllegalStateException("Study is not recruiting");
        if (applicationRepository.existsByStudyIdAndApplicantIdAndStatus(studyId, applicantId)) throw new DuplicateApplicationException("Already applied to this study");

        Application savedApplication = applicationRepository.save(
                Application.create(studyId, applicantId, answers, "지원서 제출")
        );
        notificationPort.sendApplicationSubmittedNotification(savedApplication);
        return savedApplication;
    }
}