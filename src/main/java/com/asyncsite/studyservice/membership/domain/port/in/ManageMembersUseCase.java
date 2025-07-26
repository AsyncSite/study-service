package com.asyncsite.studyservice.membership.domain.port.in;

import com.asyncsite.studyservice.membership.domain.model.MemberRole;

import java.util.UUID;

public interface ManageMembersUseCase {
    void changeMemberRole(UUID memberId, MemberRole newRole, String requesterId);
    void removeMember(UUID memberId, String requesterId);
    void warnMember(UUID memberId, String requesterId, String reason);
    void leaveStudy(UUID studyId, String userId);
}