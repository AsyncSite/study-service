package com.asyncsite.studyservice.adapter.out.notification;

import com.asyncsite.studyservice.domain.model.Study;
import com.asyncsite.studyservice.domain.port.out.NotificationPort;
import org.springframework.stereotype.Component;

@Component
public class MockNotificationAdapter implements NotificationPort {

    @Override
    public void sendStudyProposalNotification(Study study) {
        System.out.println("📧 [알림] 스터디 제안: " + study.getTitle() + " (제안자: " + study.getProposerId() + ")");
    }

    @Override
    public void sendStudyApprovalNotification(Study study) {
        System.out.println("✅ [알림] 스터디 승인: " + study.getTitle() + " (제안자: " + study.getProposerId() + ")");
    }

    @Override
    public void sendStudyRejectionNotification(Study study) {
        System.out.println("❌ [알림] 스터디 거절: " + study.getTitle() + " (제안자: " + study.getProposerId() + ")");
    }

    @Override
    public void sendStudyTerminationNotification(Study study) {
        System.out.println("🚫 [알림] 스터디 종료: " + study.getTitle() + " (제안자: " + study.getProposerId() + ")");
    }
}