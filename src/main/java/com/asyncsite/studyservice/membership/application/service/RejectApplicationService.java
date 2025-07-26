package com.asyncsite.studyservice.membership.application.service;

import com.asyncsite.studyservice.membership.domain.exception.ApplicationNotFoundException;
import com.asyncsite.studyservice.membership.domain.model.Application;
import com.asyncsite.studyservice.membership.domain.port.in.RejectApplicationUseCase;
import com.asyncsite.studyservice.membership.domain.port.out.ApplicationRepository;
import com.asyncsite.studyservice.membership.domain.port.out.MembershipNotificationPort;
import com.asyncsite.studyservice.membership.domain.service.MembershipDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RejectApplicationService implements RejectApplicationUseCase {
    private final ApplicationRepository applicationRepository;
    private final MembershipNotificationPort notificationPort;
    private final MembershipDomainService membershipDomainService;
    
    @Override
    public void reject(UUID applicationId, String reviewerId, String reason) {
        // 지원서 조회
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException("Application not found: " + applicationId));
        
        // 권한 검증
        membershipDomainService.validateMemberManagementAuthority(application.getStudyId(), reviewerId);
        
        // 지원서 거절
        application.reject(reviewerId, reason);
        applicationRepository.save(application);
        
        // 알림 발송
        notificationPort.sendApplicationRejectedNotification(application);
    }
}