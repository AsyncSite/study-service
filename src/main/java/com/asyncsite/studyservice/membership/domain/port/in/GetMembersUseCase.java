package com.asyncsite.studyservice.membership.domain.port.in;

import com.asyncsite.studyservice.membership.domain.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GetMembersUseCase {
    List<Member> getMembersByStudyId(UUID studyId);
    Page<Member> getMembersByStudyId(UUID studyId, Pageable pageable);
    Optional<Member> getMemberById(UUID memberId);
    List<Member> getMembersByUserId(String userId);
    int countMembersByStudyId(UUID studyId);
}