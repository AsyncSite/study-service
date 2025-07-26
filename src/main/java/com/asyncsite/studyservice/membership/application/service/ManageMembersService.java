package com.asyncsite.studyservice.membership.application.service;

import com.asyncsite.studyservice.membership.domain.exception.MemberNotFoundException;
import com.asyncsite.studyservice.membership.domain.model.Member;
import com.asyncsite.studyservice.membership.domain.model.MemberRole;
import com.asyncsite.studyservice.membership.domain.port.in.ManageMembersUseCase;
import com.asyncsite.studyservice.membership.domain.port.out.MemberRepository;
import com.asyncsite.studyservice.membership.domain.port.out.MembershipNotificationPort;
import com.asyncsite.studyservice.membership.domain.service.MembershipDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ManageMembersService implements ManageMembersUseCase {
    private final MemberRepository memberRepository;
    private final MembershipNotificationPort notificationPort;
    private final MembershipDomainService membershipDomainService;
    
    @Override
    public void changeMemberRole(UUID memberId, MemberRole newRole, String requesterId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found: " + memberId));
        
        // 리더 권한 검증 (역할 변경은 리더만 가능)
        membershipDomainService.validateLeaderAuthority(member.getStudyId(), requesterId);
        
        member.changeRole(newRole);
        memberRepository.save(member);
    }
    
    @Override
    public void removeMember(UUID memberId, String requesterId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found: " + memberId));
        
        // 권한 검증
        membershipDomainService.validateMemberManagementAuthority(member.getStudyId(), requesterId);
        
        member.leave();
        memberRepository.save(member);
        
        notificationPort.sendMemberLeftNotification(member.getStudyId(), member.getUserId());
    }
    
    @Override
    public void warnMember(UUID memberId, String requesterId, String reason) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found: " + memberId));
        
        // 권한 검증
        membershipDomainService.validateMemberManagementAuthority(member.getStudyId(), requesterId);
        
        member.addWarning();
        memberRepository.save(member);
        
        notificationPort.sendMemberWarningNotification(member, reason);
    }
    
    @Override
    public void leaveStudy(UUID studyId, String userId) {
        Member member = memberRepository.findByStudyIdAndUserId(studyId, userId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found in study"));
        
        member.leave();
        memberRepository.save(member);
        
        notificationPort.sendMemberLeftNotification(studyId, userId);
    }
}