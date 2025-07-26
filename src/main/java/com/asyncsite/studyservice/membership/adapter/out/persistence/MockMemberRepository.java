package com.asyncsite.studyservice.membership.adapter.out.persistence;

import com.asyncsite.studyservice.membership.domain.model.Member;
import com.asyncsite.studyservice.membership.domain.port.out.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class MockMemberRepository implements MemberRepository {
    
    private final Map<UUID, Member> storage = new ConcurrentHashMap<>();
    
    @Override
    public Member save(Member member) {
        storage.put(member.getId(), member);
        return member;
    }
    
    @Override
    public Optional<Member> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }
    
    @Override
    public List<Member> findByStudyId(UUID studyId) {
        return storage.values().stream()
                .filter(member -> member.getStudyId().equals(studyId))
                .filter(Member::isActive)
                .collect(Collectors.toList());
    }
    
    @Override
    public Page<Member> findByStudyId(UUID studyId, Pageable pageable) {
        List<Member> members = findByStudyId(studyId);
        
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), members.size());
        
        List<Member> pageContent = members.subList(start, end);
        return new PageImpl<>(pageContent, pageable, members.size());
    }
    
    @Override
    public List<Member> findByUserId(String userId) {
        return storage.values().stream()
                .filter(member -> member.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<Member> findByStudyIdAndUserId(UUID studyId, String userId) {
        return storage.values().stream()
                .filter(member -> member.getStudyId().equals(studyId) 
                        && member.getUserId().equals(userId)
                        && member.isActive())
                .findFirst();
    }
    
    @Override
    public int countByStudyId(UUID studyId) {
        return (int) storage.values().stream()
                .filter(member -> member.getStudyId().equals(studyId))
                .filter(Member::isActive)
                .count();
    }
    
    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }
}