package com.asyncsite.studyservice.study.domain.port.out;

import com.asyncsite.studyservice.study.domain.model.Study;

public interface NotificationPort {
    void sendStudyProposalNotification(Study study);
    void sendStudyApprovalNotification(Study study);
    void sendStudyRejectionNotification(Study study);
    void sendStudyTerminationNotification(Study study);
}
