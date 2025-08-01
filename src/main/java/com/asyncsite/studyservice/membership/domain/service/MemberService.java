package com.asyncsite.studyservice.membership.domain.service;

import com.asyncsite.studyservice.membership.domain.exception.MemberNotFoundException;
import com.asyncsite.studyservice.membership.domain.model.Member;
import com.asyncsite.studyservice.membership.domain.model.MemberRole;
import com.asyncsite.studyservice.membership.domain.model.MemberStatus;
import com.asyncsite.studyservice.membership.domain.port.out.MemberRepository;
import com.asyncsite.studyservice.membership.domain.port.out.MembershipNotificationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final MembershipNotificationPort notificationPort;
    private final MembershipDomainService membershipDomainService;

    @Transactional(readOnly = true)
    public Page<Member> getMembersByStudyId(UUID studyId, Pageable pageable) {
        return memberRepository.findByStudyId(studyId, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Member> getMemberById(UUID memberId) {
        return memberRepository.findById(memberId);
    }

    @Transactional(readOnly = true)
    public Optional<Member> getMemberByStudyIdAndUserId(UUID studyId, String userId) {
        return memberRepository.findByStudyIdAndUserId(studyId, userId);
    }

    @Transactional(readOnly = true)
    public int getMemberCount(UUID studyId) {
        return memberRepository.countByStudyId(studyId);
    }

    public Member changeMemberRole(UUID studyId, UUID memberId, String requesterId, MemberRole newRole) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found: " + memberId));

        membershipDomainService.validateMemberManagementAuthority(studyId, requesterId);

        member.changeRole(newRole);
        return memberRepository.save(member);
    }

    public void removeMember(UUID studyId, UUID memberId, String requesterId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found: " + memberId));

        membershipDomainService.validateMemberManagementAuthority(studyId, requesterId);

        memberRepository.deleteById(member.getId());
        notificationPort.sendMemberLeftNotification(studyId, member.getUserId());
    }

    public void warnMember(UUID studyId, UUID memberId, String requesterId, String reason) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found: " + memberId));

        membershipDomainService.validateMemberManagementAuthority(studyId, requesterId);

        member.warn();
        memberRepository.save(member);
        notificationPort.sendMemberWarningNotification(member, reason);
    }

    public void leaveStudy(UUID studyId, String userId) {
        Member member = memberRepository.findByStudyIdAndUserId(studyId, userId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found: " + userId + " in study " + studyId));

        member.leave();
        memberRepository.save(member);
        notificationPort.sendMemberLeftNotification(studyId, userId);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getMemberStatistics(UUID studyId) {
        int totalMembers = memberRepository.countByStudyId(studyId);
        int activeMembers = memberRepository.countByStudyIdAndStatus(studyId, MemberStatus.ACTIVE);
        int inactiveMembers = memberRepository.countByStudyIdAndStatus(studyId, MemberStatus.DORMANT);
        Map<MemberRole, Long> roleDistribution = memberRepository.countMembersByRole(studyId);

        // TODO: Implement actual averageAttendanceRate and warningCount logic
        double averageAttendanceRate = 0.0; // Placeholder
        Map<String, Integer> warningCountMap = new HashMap<>();
        memberRepository.findByStudyId(studyId).stream()
                .filter(member -> member.getWarningCount() != null)
                .collect(Collectors.groupingBy(Member::getWarningCount, Collectors.counting()))
                .forEach((count, numMembers) -> warningCountMap.put(String.valueOf(count), numMembers.intValue()));

        // Ensure 0, 1, 2 warning counts are present
        warningCountMap.putIfAbsent("0", 0);
        warningCountMap.putIfAbsent("1", 0);
        warningCountMap.putIfAbsent("2", 0);

        Map<String, Integer> finalWarningCount = warningCountMap;

        return Map.of(
                "totalMembers", totalMembers,
                "activeMembers", activeMembers,
                "inactiveMembers", inactiveMembers,
                "averageAttendanceRate", averageAttendanceRate,
                "warningCount", finalWarningCount,
                "roleDistribution", roleDistribution
        );
    }
}