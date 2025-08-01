package com.asyncsite.studyservice.membership.adapter.out.persistence;

import com.asyncsite.studyservice.membership.adapter.out.persistence.entity.MemberJpaEntity;
import com.asyncsite.studyservice.membership.adapter.out.persistence.mapper.MemberPersistenceMapper;
import com.asyncsite.studyservice.membership.adapter.out.persistence.repository.MemberJpaRepository;
import com.asyncsite.studyservice.membership.domain.model.Member;
import com.asyncsite.studyservice.membership.domain.model.MemberRole;
import com.asyncsite.studyservice.membership.domain.model.MemberStatus;
import com.asyncsite.studyservice.membership.domain.port.out.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;
    private final MemberPersistenceMapper memberPersistenceMapper;

    @Override
    public Member save(Member member) {
        MemberJpaEntity entity = memberPersistenceMapper.toEntity(member);
        MemberJpaEntity savedEntity = memberJpaRepository.save(entity);
        return memberPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Member> findById(UUID id) {
        return memberJpaRepository.findById(id)
                .map(memberPersistenceMapper::toDomain);
    }

    @Override
    public List<Member> findByStudyId(UUID studyId) {
        return memberJpaRepository.findByStudyId(studyId, Pageable.unpaged()).getContent().stream()
                .map(memberPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Member> findByStudyId(UUID studyId, Pageable pageable) {
        return memberJpaRepository.findByStudyId(studyId, pageable)
                .map(memberPersistenceMapper::toDomain);
    }

    @Override
    public List<Member> findByUserId(String userId) {
        return memberJpaRepository.findByUserId(userId).stream()
                .map(memberPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Member> findByStudyIdAndUserId(UUID studyId, String userId) {
        return memberJpaRepository.findByStudyIdAndUserId(studyId, userId)
                .map(memberPersistenceMapper::toDomain);
    }

    @Override
    public int countByStudyId(UUID studyId) {
        return memberJpaRepository.countByStudyId(studyId);
    }

    @Override
    public int countByStudyIdAndStatus(UUID studyId, MemberStatus status) {
        return memberJpaRepository.countByStudyIdAndStatus(studyId, status);
    }

    @Override
    public Map<MemberRole, Long> countMembersByRole(UUID studyId) {
        return memberJpaRepository.countMembersByRole(studyId).stream()
                .collect(Collectors.toMap(
                        row -> (MemberRole) row[0],
                        row -> (Long) row[1]
                ));
    }

    @Override
    public void deleteById(UUID id) {
        memberJpaRepository.deleteById(id);
    }
}