package com.asyncsite.studyservice.membership.application.service;

import com.asyncsite.studyservice.membership.domain.exception.ApplicationNotFoundException;
import com.asyncsite.studyservice.membership.domain.model.Application;
import com.asyncsite.studyservice.membership.domain.model.Member;
import com.asyncsite.studyservice.membership.domain.port.in.AcceptApplicationUseCase;
import com.asyncsite.studyservice.membership.domain.port.out.ApplicationRepository;
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
public class AcceptApplicationService implements AcceptApplicationUseCase {
    private final ApplicationRepository applicationRepository;
    private final MemberRepository memberRepository;
    private final MembershipNotificationPort notificationPort;
    private final MembershipDomainService membershipDomainService;
    
    @Override
    public Member accept(UUID applicationId, String reviewerId, String note) {
        // 지원서 조회
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException("Application not found: " + applicationId));
        
        // 권한 검증
        membershipDomainService.validateMemberManagementAuthority(application.getStudyId(), reviewerId);
        
        // 지원서 승인
        application.accept(reviewerId, note);
        applicationRepository.save(application);
        
        // 멤버 생성
        Member member = Member.createFromApplication(application);
        Member savedMember = memberRepository.save(member);
        
        // 알림 발송
        notificationPort.sendApplicationAcceptedNotification(application);
        notificationPort.sendMemberJoinedNotification(savedMember);
        
        return savedMember;
    }
}