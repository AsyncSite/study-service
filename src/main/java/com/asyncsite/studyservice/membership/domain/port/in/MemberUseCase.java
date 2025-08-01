package com.asyncsite.studyservice.membership.domain.port.in;

import com.asyncsite.studyservice.membership.domain.model.Member;
import com.asyncsite.studyservice.membership.domain.model.MemberRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface MemberUseCase {
    Page<Member> getMembers(UUID studyId, Pageable pageable);
    Optional<Member> getMember(UUID studyId, UUID memberId);
    int getMemberCount(UUID studyId);
    Member changeMemberRole(UUID studyId, UUID memberId, String requesterId, MemberRole newRole);
    void removeMember(UUID studyId, UUID memberId, String requesterId);
    void warnMember(UUID studyId, UUID memberId, String requesterId, String reason);
    void leaveStudy(UUID studyId, String userId);
    Map<String, Object> getMemberStatistics(UUID studyId);
}