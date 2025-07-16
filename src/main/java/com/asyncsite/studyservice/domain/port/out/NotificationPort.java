package com.asyncsite.studyservice.domain.port.out;

import com.asyncsite.studyservice.domain.model.Study;

public interface NotificationPort {
    void sendStudyProposalNotification(Study study);
    void sendStudyApprovalNotification(Study study);
    void sendStudyRejectionNotification(Study study);
    void sendStudyTerminationNotification(Study study);
}