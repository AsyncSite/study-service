package com.asyncsite.studyservice.membership.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApplicationStatus {
    PENDING("대기중"),
    ACCEPTED("승인"),
    REJECTED("거절"),
    CANCELLED("취소");
    
    private final String description;
    
    public boolean canBeProcessed() {
        return this == PENDING;
    }
}