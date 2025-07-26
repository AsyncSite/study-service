package com.asyncsite.studyservice.membership.application.service;

import com.asyncsite.studyservice.membership.domain.exception.DuplicateApplicationException;
import com.asyncsite.studyservice.membership.domain.model.Application;
import com.asyncsite.studyservice.membership.domain.port.in.ApplyToStudyUseCase;
import com.asyncsite.studyservice.membership.domain.port.out.ApplicationRepository;
import com.asyncsite.studyservice.membership.domain.port.out.MembershipNotificationPort;
import com.asyncsite.studyservice.membership.domain.port.out.StudyValidationPort;
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
    private final StudyValidationPort studyValidationPort;
    private final MembershipNotificationPort notificationPort;
    
    @Override
    public Application apply(UUID studyId, String applicantId, Map<String, String> answers) {
        // 스터디 존재 및 모집 중 검증
        if (!studyValidationPort.isStudyExists(studyId)) {
            throw new IllegalArgumentException("Study not found: " + studyId);
        }
        
        if (!studyValidationPort.isStudyRecruiting(studyId)) {
            throw new IllegalStateException("Study is not recruiting");
        }
        
        // 중복 지원 검증
        if (applicationRepository.existsByStudyIdAndApplicantId(studyId, applicantId)) {
            throw new DuplicateApplicationException("Already applied to this study");
        }
        
        // Map<String, String>을 Map<String, Object>로 변환
        Map<String, Object> answersObject = new java.util.HashMap<>(answers);
        
        // 지원서 생성
        Application application = Application.create(studyId, applicantId, answersObject, "지원서 제출");
        Application savedApplication = applicationRepository.save(application);
        
        // 알림 발송
        notificationPort.sendApplicationSubmittedNotification(savedApplication);
        
        return savedApplication;
    }
}