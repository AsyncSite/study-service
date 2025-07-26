package com.asyncsite.studyservice.membership.application.service;

import com.asyncsite.studyservice.membership.domain.model.Member;
import com.asyncsite.studyservice.membership.domain.port.in.QueryMembersUseCase;
import com.asyncsite.studyservice.membership.domain.port.out.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryMembersService implements QueryMembersUseCase {
    private final MemberRepository memberRepository;
    
    @Override
    public Page<Member> getMembersByStudyId(UUID studyId, Pageable pageable) {
        return memberRepository.findByStudyId(studyId, pageable);
    }
    
    @Override
    public Optional<Member> getMemberById(UUID memberId) {
        return memberRepository.findById(memberId);
    }
    
    @Override
    public Optional<Member> getMemberByStudyIdAndUserId(UUID studyId, String userId) {
        return memberRepository.findByStudyIdAndUserId(studyId, userId);
    }
}