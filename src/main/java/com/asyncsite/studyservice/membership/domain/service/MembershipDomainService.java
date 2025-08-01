package com.asyncsite.studyservice.membership.domain.service;

import com.asyncsite.studyservice.membership.domain.exception.UnauthorizedMemberActionException;
import com.asyncsite.studyservice.membership.domain.model.Member;
import com.asyncsite.studyservice.membership.domain.model.MemberRole;
import com.asyncsite.studyservice.membership.domain.port.out.MemberRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class MembershipDomainService {
    private final MemberRepository memberRepository;
    
    public boolean canManageMembers(UUID studyId, String userId) {
        return memberRepository.findByStudyIdAndUserId(studyId, userId)
                .map(Member::canManageMembers)
                .orElse(false);
    }
    
    public void validateMemberManagementAuthority(UUID studyId, String requesterId) {
        if (!canManageMembers(studyId, requesterId)) {
            throw new UnauthorizedMemberActionException("User does not have authority to manage members");
        }
    }
    
    public void validateLeaderAuthority(UUID studyId, String requesterId) {
        Member requester = memberRepository.findByStudyIdAndUserId(studyId, requesterId)
                .orElseThrow(() -> new UnauthorizedMemberActionException("Requester is not a member"));
                
        if (!requester.isLeader()) {
            throw new UnauthorizedMemberActionException("Only leader can perform this action");
        }
    }
    
    public int countActiveMembers(UUID studyId) {
        return memberRepository.countByStudyId(studyId);
    }
    
    public boolean isStudyFull(UUID studyId, int maxMembers) {
        return countActiveMembers(studyId) >= maxMembers;
    }
}