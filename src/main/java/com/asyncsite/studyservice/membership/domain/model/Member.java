package com.asyncsite.studyservice.membership.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private UUID id;
    private UUID studyId;
    private String userId;
    private MemberRole role;
    private MemberStatus status;
    private LocalDateTime joinedAt;
    private LocalDateTime lastActiveAt;
    private Integer warningCount;
    private String introduction;
    private LocalDateTime leftAt;
    
    // 비즈니스 메서드
    public boolean isActive() {
        return status == MemberStatus.ACTIVE;
    }
    
    public boolean isManager() {
        return role == MemberRole.MANAGER || role == MemberRole.OWNER;
    }
    
    public boolean canManageMembers() {
        return role == MemberRole.OWNER || role == MemberRole.MANAGER;
    }
    
    public void warn() {
        this.warningCount = (this.warningCount == null ? 0 : this.warningCount) + 1;
        if (this.warningCount >= 3) {
            this.status = MemberStatus.SUSPENDED;
        }
    }
    
    public void changeRole(MemberRole newRole) {
        this.role = newRole;
    }
    
    public void updateLastActiveTime() {
        this.lastActiveAt = LocalDateTime.now();
    }
    
    public void suspend() {
        this.status = MemberStatus.SUSPENDED;
    }
    
    public void activate() {
        this.status = MemberStatus.ACTIVE;
        this.warningCount = 0;
    }
    
    public void withdraw() {
        this.status = MemberStatus.WITHDRAWN;
    }
    
    public void leave() {
        this.status = MemberStatus.WITHDRAWN;
        this.leftAt = LocalDateTime.now();
    }
    
    public boolean isLeader() {
        return this.role == MemberRole.OWNER;
    }
    
    public void addWarning() {
        warn(); // 기존 warn() 메서드를 재사용
    }
    
    // 정적 팩토리 메서드
    public static Member createMember(UUID studyId, String userId, MemberRole role) {
        return Member.builder()
                .id(UUID.randomUUID())
                .studyId(studyId)
                .userId(userId)
                .role(role)
                .status(MemberStatus.ACTIVE)
                .joinedAt(LocalDateTime.now())
                .lastActiveAt(LocalDateTime.now())
                .warningCount(0)
                .build();
    }
    
    public static Member createFromApplication(Application application) {
        return Member.builder()
                .id(UUID.randomUUID())
                .studyId(application.getStudyId())
                .userId(application.getApplicantId())
                .role(MemberRole.MEMBER)
                .status(MemberStatus.ACTIVE)
                .joinedAt(LocalDateTime.now())
                .lastActiveAt(LocalDateTime.now())
                .warningCount(0)
                .build();
    }
}