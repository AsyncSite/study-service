package com.asyncsite.studyservice.membership.domain.port.out;

import com.asyncsite.studyservice.membership.domain.model.Application;
import com.asyncsite.studyservice.membership.domain.model.Member;

import java.util.UUID;

public interface MembershipNotificationPort {
    void sendApplicationSubmittedNotification(Application application);
    void sendApplicationAcceptedNotification(Application application);
    void sendApplicationRejectedNotification(Application application);
    void sendMemberJoinedNotification(Member member);
    void sendMemberLeftNotification(UUID studyId, String userId);
    void sendMemberWarningNotification(Member member, String reason);
}