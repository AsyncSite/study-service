package com.asyncsite.studyservice.membership.application.service;

import com.asyncsite.studyservice.membership.domain.model.Member;
import com.asyncsite.studyservice.membership.domain.model.MemberRole;
import com.asyncsite.studyservice.membership.domain.port.in.MemberUseCase;
import com.asyncsite.studyservice.membership.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberUseCaseImpl implements MemberUseCase {
    private final MemberService memberService;

    @Override
    public Page<Member> getMembers(UUID studyId, Pageable pageable) {
        return memberService.getMembersByStudyId(studyId, pageable);
    }

    @Override
    public Optional<Member> getMember(UUID studyId, UUID memberId) {
        return memberService.getMemberById(memberId);
    }

    @Override
    public int getMemberCount(UUID studyId) {
        return memberService.getMemberCount(studyId);
    }

    @Override
    public Member changeMemberRole(UUID studyId, UUID memberId, String requesterId, MemberRole newRole) {
        return memberService.changeMemberRole(studyId, memberId, requesterId, newRole);
    }

    @Override
    public void removeMember(UUID studyId, UUID memberId, String requesterId) {
        memberService.removeMember(studyId, memberId, requesterId);
    }

    @Override
    public void warnMember(UUID studyId, UUID memberId, String requesterId, String reason) {
        memberService.warnMember(studyId, memberId, requesterId, reason);
    }

    @Override
    public void leaveStudy(UUID studyId, String userId) {
        memberService.leaveStudy(studyId, userId);
    }

    @Override
    public Map<String, Object> getMemberStatistics(UUID studyId) {
        return memberService.getMemberStatistics(studyId);
    }
}