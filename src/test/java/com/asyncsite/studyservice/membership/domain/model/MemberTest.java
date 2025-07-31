package com.asyncsite.studyservice.membership.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Member 도메인 모델 테스트")
class MemberTest {

    @Test
    @DisplayName("Member 객체를 생성할 수 있다")
    void createMember() {
        UUID studyId = UUID.randomUUID();
        String userId = "user1";
        MemberRole role = MemberRole.MEMBER;

        Member member = Member.createMember(studyId, userId, role);

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStudyId()).isEqualTo(studyId);
        assertThat(member.getUserId()).isEqualTo(userId);
        assertThat(member.getRole()).isEqualTo(role);
        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(member.getJoinedAt()).isNotNull();
        assertThat(member.getLastActiveAt()).isNotNull();
        assertThat(member.getWarningCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("Application으로부터 Member 객체를 생성할 수 있다")
    void createMemberFromApplication() {
        UUID studyId = UUID.randomUUID();
        String applicantId = "applicant1";
        Application application = Application.create(studyId, "Test Study Title", applicantId, null, null);

        Member member = Member.createFromApplication(application);

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStudyId()).isEqualTo(studyId);
        assertThat(member.getUserId()).isEqualTo(applicantId);
        assertThat(member.getRole()).isEqualTo(MemberRole.MEMBER);
        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(member.getJoinedAt()).isNotNull();
        assertThat(member.getLastActiveAt()).isNotNull();
        assertThat(member.getWarningCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("멤버가 활동 중인지 확인할 수 있다")
    void isActive() {
        Member activeMember = Member.createMember(UUID.randomUUID(), "user1", MemberRole.MEMBER);
        assertThat(activeMember.isActive()).isTrue();

        Member suspendedMember = Member.createMember(UUID.randomUUID(), "user2", MemberRole.MEMBER);
        suspendedMember.suspend();
        assertThat(suspendedMember.isActive()).isFalse();
    }

    @Test
    @DisplayName("멤버가 매니저 권한을 가지고 있는지 확인할 수 있다")
    void isManager() {
        Member owner = Member.createMember(UUID.randomUUID(), "user1", MemberRole.OWNER);
        assertThat(owner.isManager()).isTrue();

        Member manager = Member.createMember(UUID.randomUUID(), "user2", MemberRole.MANAGER);
        assertThat(manager.isManager()).isTrue();

        Member member = Member.createMember(UUID.randomUUID(), "user3", MemberRole.MEMBER);
        assertThat(member.isManager()).isFalse();
    }

    @Test
    @DisplayName("멤버가 멤버 관리 권한을 가지고 있는지 확인할 수 있다")
    void canManageMembers() {
        Member owner = Member.createMember(UUID.randomUUID(), "user1", MemberRole.OWNER);
        assertThat(owner.canManageMembers()).isTrue();

        Member manager = Member.createMember(UUID.randomUUID(), "user2", MemberRole.MANAGER);
        assertThat(manager.canManageMembers()).isTrue();

        Member member = Member.createMember(UUID.randomUUID(), "user3", MemberRole.MEMBER);
        assertThat(member.canManageMembers()).isFalse();
    }

    @Test
    @DisplayName("멤버에게 경고를 줄 수 있다")
    void warnMember() {
        Member member = Member.createMember(UUID.randomUUID(), "user1", MemberRole.MEMBER);
        member.warn();
        assertThat(member.getWarningCount()).isEqualTo(1);

        member.warn();
        member.warn();
        assertThat(member.getWarningCount()).isEqualTo(3);
        assertThat(member.getStatus()).isEqualTo(MemberStatus.SUSPENDED);
    }

    @Test
    @DisplayName("멤버 역할을 변경할 수 있다")
    void changeRole() {
        Member member = Member.createMember(UUID.randomUUID(), "user1", MemberRole.MEMBER);
        member.changeRole(MemberRole.MANAGER);
        assertThat(member.getRole()).isEqualTo(MemberRole.MANAGER);
    }

    @Test
    @DisplayName("마지막 활동 시간을 업데이트할 수 있다")
    void updateLastActiveTime() {
        Member member = Member.createMember(UUID.randomUUID(), "user1", MemberRole.MEMBER);
        LocalDateTime oldTime = member.getLastActiveAt();
        member.updateLastActiveTime();
        assertThat(member.getLastActiveAt()).isAfter(oldTime);
    }

    @Test
    @DisplayName("멤버를 정지시킬 수 있다")
    void suspendMember() {
        Member member = Member.createMember(UUID.randomUUID(), "user1", MemberRole.MEMBER);
        member.suspend();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.SUSPENDED);
    }

    @Test
    @DisplayName("멤버를 활성화할 수 있다")
    void activateMember() {
        Member member = Member.createMember(UUID.randomUUID(), "user1", MemberRole.MEMBER);
        member.suspend();
        member.warn(); // Add warning to test reset
        member.activate();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(member.getWarningCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("멤버를 탈퇴시킬 수 있다")
    void withdrawMember() {
        Member member = Member.createMember(UUID.randomUUID(), "user1", MemberRole.MEMBER);
        member.withdraw();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.WITHDRAWN);
    }

    @Test
    @DisplayName("스터디를 떠날 수 있다")
    void leaveStudy() {
        Member member = Member.createMember(UUID.randomUUID(), "user1", MemberRole.MEMBER);
        member.leave();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.WITHDRAWN);
        assertThat(member.getLeftAt()).isNotNull();
    }

    @Test
    @DisplayName("멤버가 리더인지 확인할 수 있다")
    void isLeader() {
        Member owner = Member.createMember(UUID.randomUUID(), "user1", MemberRole.OWNER);
        assertThat(owner.isLeader()).isTrue();

        Member member = Member.createMember(UUID.randomUUID(), "user2", MemberRole.MEMBER);
        assertThat(member.isLeader()).isFalse();
    }

    @Test
    @DisplayName("경고를 추가할 수 있다 (warn 메서드 재사용)")
    void addWarningReusesWarn() {
        Member member = Member.createMember(UUID.randomUUID(), "user1", MemberRole.MEMBER);
        member.addWarning();
        assertThat(member.getWarningCount()).isEqualTo(1);
    }
}
