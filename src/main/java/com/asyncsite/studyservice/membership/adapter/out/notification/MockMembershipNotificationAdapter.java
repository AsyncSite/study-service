package com.asyncsite.studyservice.membership.adapter.out.notification;

import com.asyncsite.studyservice.membership.domain.model.Application;
import com.asyncsite.studyservice.membership.domain.model.Member;
import com.asyncsite.studyservice.membership.domain.port.out.MembershipNotificationPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class MockMembershipNotificationAdapter implements MembershipNotificationPort {
    
    @Override
    public void sendApplicationSubmittedNotification(Application application) {
        log.info("Mock: Application submitted notification sent for application: {} to study: {}", 
                application.getId(), application.getStudyId());
    }
    
    @Override
    public void sendApplicationAcceptedNotification(Application application) {
        log.info("Mock: Application accepted notification sent to applicant: {} for study: {}", 
                application.getApplicantId(), application.getStudyId());
    }
    
    @Override
    public void sendApplicationRejectedNotification(Application application) {
        log.info("Mock: Application rejected notification sent to applicant: {} for study: {} with reason: {}", 
                application.getApplicantId(), application.getStudyId(), application.getReviewNote());
    }
    
    @Override
    public void sendMemberJoinedNotification(Member member) {
        log.info("Mock: New member joined notification sent for user: {} in study: {}", 
                member.getUserId(), member.getStudyId());
    }
    
    @Override
    public void sendMemberLeftNotification(UUID studyId, String userId) {
        log.info("Mock: Member left notification sent for user: {} from study: {}", 
                userId, studyId);
    }
    
    @Override
    public void sendMemberWarningNotification(Member member, String reason) {
        log.info("Mock: Warning notification sent to member: {} in study: {} with reason: {}", 
                member.getUserId(), member.getStudyId(), reason);
    }

    @Override
    public void sendApplicationCancelledNotification(Application application) {
        log.info("Mock: Application cancelled notification sent for application: {} to study: {}",
                application.getId(), application.getStudyId());
    }
}