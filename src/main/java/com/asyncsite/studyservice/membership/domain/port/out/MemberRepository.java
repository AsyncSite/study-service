package com.asyncsite.studyservice.membership.domain.port.out;

import com.asyncsite.studyservice.membership.domain.model.Member;
import com.asyncsite.studyservice.membership.domain.model.MemberRole;
import com.asyncsite.studyservice.membership.domain.model.MemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(UUID id);
    List<Member> findByStudyId(UUID studyId);
    Page<Member> findByStudyId(UUID studyId, Pageable pageable);
    List<Member> findByUserId(String userId);
    Optional<Member> findByStudyIdAndUserId(UUID studyId, String userId);
    int countByStudyId(UUID studyId);
    int countByStudyIdAndStatus(UUID studyId, MemberStatus status);
    Map<MemberRole, Long> countMembersByRole(UUID studyId);
    void deleteById(UUID id);
}