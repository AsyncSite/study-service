package com.asyncsite.studyservice.membership.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberStatus {
    ACTIVE("활동중"),
    SUSPENDED("정지"),
    WITHDRAWN("탈퇴"),
    DORMANT("휴면");
    
    private final String description;
    
    public boolean isActive() {
        return this == ACTIVE;
    }
    
    public boolean canParticipate() {
        return this == ACTIVE;
    }
}