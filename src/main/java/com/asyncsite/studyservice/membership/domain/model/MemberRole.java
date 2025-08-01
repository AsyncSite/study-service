package com.asyncsite.studyservice.membership.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    OWNER("스터디장", 1),
    MANAGER("매니저", 2),
    MEMBER("일반 멤버", 3),
    GUEST("게스트", 4);
    
    private final String description;
    private final int level;
    
    public boolean isHigherThan(MemberRole other) {
        return this.level < other.level;
    }
    
    public boolean canManage() {
        return this == OWNER || this == MANAGER;
    }
}