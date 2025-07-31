package com.asyncsite.studyservice.membership.application.service;

import com.asyncsite.studyservice.membership.domain.model.Member;
import com.asyncsite.studyservice.membership.domain.model.MemberRole;
import com.asyncsite.studyservice.membership.domain.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("MemberUseCaseImpl 테스트")
class MemberUseCaseImplTest {

    @Mock
    private MemberService memberService;

    private MemberUseCaseImpl memberUseCase;

    @BeforeEach
    void setUp() {
        memberUseCase = new MemberUseCaseImpl(memberService);
    }

    @Test
    @DisplayName("멤버 목록을 조회할 수 있다")
    void givenStudyId_whenGetMembers_thenReturnsMemberPage() {
        // given
        final UUID studyId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);
        List<Member> members = List.of(Member.builder().build());
        Page<Member> expectedPage = new PageImpl<>(members, pageable, members.size());

        given(memberService.getMembersByStudyId(studyId, pageable)).willReturn(expectedPage);

        // when
        Page<Member> result = memberUseCase.getMembers(studyId, pageable);

        // then
        assertThat(result).isEqualTo(expectedPage);
        verify(memberService).getMembersByStudyId(studyId, pageable);
    }

    @Test
    @DisplayName("멤버를 단건 조회할 수 있다")
    void givenStudyIdAndMemberId_whenGetMember_thenReturnsMember() {
        // given
        final UUID studyId = UUID.randomUUID();
        final UUID memberId = UUID.randomUUID();
        Member expectedMember = Member.builder().build();

        given(memberService.getMemberById(memberId)).willReturn(Optional.of(expectedMember));

        // when
        Optional<Member> result = memberUseCase.getMember(studyId, memberId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expectedMember);
        verify(memberService).getMemberById(memberId);
    }

    @Test
    @DisplayName("멤버 수를 조회할 수 있다")
    void givenStudyId_whenGetMemberCount_thenReturnsCount() {
        // given
        final UUID studyId = UUID.randomUUID();
        final int expectedCount = 5;

        given(memberService.getMemberCount(studyId)).willReturn(expectedCount);

        // when
        int result = memberUseCase.getMemberCount(studyId);

        // then
        assertThat(result).isEqualTo(expectedCount);
        verify(memberService).getMemberCount(studyId);
    }

    @Test
    @DisplayName("멤버 역할을 변경할 수 있다")
    void givenStudyIdMemberIdRequesterIdAndNewRole_whenChangeMemberRole_thenReturnsUpdatedMember() {
        // given
        final UUID studyId = UUID.randomUUID();
        final UUID memberId = UUID.randomUUID();
        final String requesterId = "requester-id";
        final MemberRole newRole = MemberRole.MANAGER;
        Member expectedMember = Member.builder().build();

        given(memberService.changeMemberRole(studyId, memberId, requesterId, newRole)).willReturn(expectedMember);

        // when
        Member result = memberUseCase.changeMemberRole(studyId, memberId, requesterId, newRole);

        // then
        assertThat(result).isEqualTo(expectedMember);
        verify(memberService).changeMemberRole(studyId, memberId, requesterId, newRole);
    }

    @Test
    @DisplayName("멤버를 제명할 수 있다")
    void givenStudyIdMemberIdAndRequesterId_whenRemoveMember_thenCallsMemberServiceRemoveMember() {
        // given
        final UUID studyId = UUID.randomUUID();
        final UUID memberId = UUID.randomUUID();
        final String requesterId = "requester-id";

        // when
        memberUseCase.removeMember(studyId, memberId, requesterId);

        // then
        verify(memberService).removeMember(studyId, memberId, requesterId);
    }

    @Test
    @DisplayName("멤버에게 경고를 발송할 수 있다")
    void givenStudyIdMemberIdRequesterIdAndReason_whenWarnMember_thenCallsMemberServiceWarnMember() {
        // given
        final UUID studyId = UUID.randomUUID();
        final UUID memberId = UUID.randomUUID();
        final String requesterId = "requester-id";
        final String reason = "reason";

        // when
        memberUseCase.warnMember(studyId, memberId, requesterId, reason);

        // then
        verify(memberService).warnMember(studyId, memberId, requesterId, reason);
    }

    @Test
    @DisplayName("스터디를 탈퇴할 수 있다")
    void givenStudyIdAndUserId_whenLeaveStudy_thenCallsMemberServiceLeaveStudy() {
        // given
        final UUID studyId = UUID.randomUUID();
        final String userId = "user-id";

        // when
        memberUseCase.leaveStudy(studyId, userId);

        // then
        verify(memberService).leaveStudy(studyId, userId);
    }

    @Test
    @DisplayName("멤버 통계를 조회할 수 있다")
    void givenStudyId_whenGetMemberStatistics_thenReturnsStatisticsMap() {
        // given
        final UUID studyId = UUID.randomUUID();
        Map<String, Object> expectedStatistics = Map.of(
                "totalMembers", 10,
                "activeMembers", 8,
                "inactiveMembers", 2,
                "averageAttendanceRate", 75.0,
                "warningCount", Map.of("0", 7, "1", 2, "2", 1),
                "roleDistribution", Map.of(MemberRole.OWNER, 1L, MemberRole.MEMBER, 9L)
        );

        given(memberService.getMemberStatistics(studyId)).willReturn(expectedStatistics);

        // when
        Map<String, Object> result = memberUseCase.getMemberStatistics(studyId);

        // then
        assertThat(result).isEqualTo(expectedStatistics);
        verify(memberService).getMemberStatistics(studyId);
    }
}
