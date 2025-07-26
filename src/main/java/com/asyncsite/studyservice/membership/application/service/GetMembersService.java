package com.asyncsite.studyservice.membership.application.service;

import com.asyncsite.studyservice.membership.domain.model.Member;
import com.asyncsite.studyservice.membership.domain.port.in.GetMembersUseCase;
import com.asyncsite.studyservice.membership.domain.port.out.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetMembersService implements GetMembersUseCase {
    private final MemberRepository memberRepository;
    
    @Override
    public List<Member> getMembersByStudyId(UUID studyId) {
        return memberRepository.findByStudyId(studyId);
    }
    
    @Override
    public Page<Member> getMembersByStudyId(UUID studyId, Pageable pageable) {
        return memberRepository.findByStudyId(studyId, pageable);
    }
    
    @Override
    public Optional<Member> getMemberById(UUID memberId) {
        return memberRepository.findById(memberId);
    }
    
    @Override
    public List<Member> getMembersByUserId(String userId) {
        return memberRepository.findByUserId(userId);
    }
    
    @Override
    public int countMembersByStudyId(UUID studyId) {
        return memberRepository.countByStudyId(studyId);
    }
}