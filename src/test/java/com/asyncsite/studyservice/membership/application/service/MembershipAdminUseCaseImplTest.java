package com.asyncsite.studyservice.membership.application.service;

import com.asyncsite.studyservice.membership.adapter.in.web.MembershipAdminControllerDocs.ApplicationAdminResponse;
import com.asyncsite.studyservice.membership.domain.service.MembershipAdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("MembershipAdminUseCaseImpl 테스트")
class MembershipAdminUseCaseImplTest {

    @Mock
    private MembershipAdminService membershipAdminService;

    private MembershipAdminUseCaseImpl membershipAdminUseCase;

    @BeforeEach
    void setUp() {
        membershipAdminUseCase = new MembershipAdminUseCaseImpl(membershipAdminService);
    }

    @Test
    @DisplayName("장기 미처리 지원을 조회할 수 있다")
    void givenDays_whenGetPendingApplications_thenReturnsListOfApplicationAdminResponse() {
        // given
        int days = 7;
        List<ApplicationAdminResponse> expectedList = List.of(ApplicationAdminResponse.createMock("Test Study", 8));
        given(membershipAdminService.getPendingApplications(days)).willReturn(expectedList);

        // when
        List<ApplicationAdminResponse> result = membershipAdminUseCase.getPendingApplications(days);

        // then
        assertThat(result).isEqualTo(expectedList);
        verify(membershipAdminService).getPendingApplications(days);
    }

    @Test
    @DisplayName("비활성 멤버를 조회할 수 있다")
    void givenDays_whenGetInactiveMembers_thenReturnsListOfMap() {
        // given
        int days = 30;
        List<Map<String, Object>> expectedList = List.of(Map.of("memberId", UUID.randomUUID().toString()));
        given(membershipAdminService.getInactiveMembers(days)).willReturn(expectedList);

        // when
        List<Map<String, Object>> result = membershipAdminUseCase.getInactiveMembers(days);

        // then
        assertThat(result).isEqualTo(expectedList);
        verify(membershipAdminService).getInactiveMembers(days);
    }

    @Test
    @DisplayName("스터디 멤버를 초기화할 수 있다")
    void givenStudyId_whenResetStudyMembers_thenReturnsMap() {
        // given
        UUID studyId = UUID.randomUUID();
        Map<String, Object> expectedMap = Map.of("studyId", studyId.toString());
        given(membershipAdminService.resetStudyMembers(studyId)).willReturn(expectedMap);

        // when
        Map<String, Object> result = membershipAdminUseCase.resetStudyMembers(studyId);

        // then
        assertThat(result).isEqualTo(expectedMap);
        verify(membershipAdminService).resetStudyMembers(studyId);
    }

    @Test
    @DisplayName("멤버 이탈률 리포트를 조회할 수 있다")
    void givenDays_whenGetMemberChurnReport_thenReturnsMap() {
        // given
        int days = 30;
        Map<String, Object> expectedMap = Map.of("totalChurn", 10);
        given(membershipAdminService.getMemberChurnReport(days)).willReturn(expectedMap);

        // when
        Map<String, Object> result = membershipAdminUseCase.getMemberChurnReport(days);

        // then
        assertThat(result).isEqualTo(expectedMap);
        verify(membershipAdminService).getMemberChurnReport(days);
    }

    @Test
    @DisplayName("멤버십 통계 개요를 조회할 수 있다")
    void whenGetMembershipStatistics_thenReturnsMap() {
        // given
        Map<String, Object> expectedMap = Map.of("totalApplications", 100);
        given(membershipAdminService.getMembershipStatistics()).willReturn(expectedMap);

        // when
        Map<String, Object> result = membershipAdminUseCase.getMembershipStatistics();

        // then
        assertThat(result).isEqualTo(expectedMap);
        verify(membershipAdminService).getMembershipStatistics();
    }
}
