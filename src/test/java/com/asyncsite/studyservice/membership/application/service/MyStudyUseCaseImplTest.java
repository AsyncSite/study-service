package com.asyncsite.studyservice.membership.application.service;

import com.asyncsite.studyservice.membership.domain.model.Application;
import com.asyncsite.studyservice.membership.domain.model.Member;
import com.asyncsite.studyservice.membership.domain.port.in.MyStudyUseCase;
import com.asyncsite.studyservice.membership.domain.service.MyStudyService;
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
@DisplayName("MyStudyUseCaseImpl 테스트")
class MyStudyUseCaseImplTest {

    @Mock
    private MyStudyService myStudyService;

    private MyStudyUseCase myStudyUseCase;

    @BeforeEach
    void setUp() {
        myStudyUseCase = new MyStudyUseCaseImpl(myStudyService);
    }

    @Test
    @DisplayName("내 지원 현황을 조회할 수 있다")
    void givenUserId_whenGetMyApplications_thenReturnsListOfApplications() {
        // given
        final String userId = "user123";
        List<Application> expectedApplications = List.of(Application.builder().build());

        given(myStudyService.getMyApplications(userId)).willReturn(expectedApplications);

        // when
        List<Application> result = myStudyUseCase.getMyApplications(userId);

        // then
        assertThat(result).isEqualTo(expectedApplications);
        verify(myStudyService).getMyApplications(userId);
    }

    @Test
    @DisplayName("내가 참여중인 스터디를 조회할 수 있다")
    void givenUserId_whenGetMyStudies_thenReturnsListOfMembers() {
        // given
        final String userId = "user123";
        List<Member> expectedMembers = List.of(Member.builder().build());

        given(myStudyService.getMyStudies(userId)).willReturn(expectedMembers);

        // when
        List<Member> result = myStudyUseCase.getMyStudies(userId);

        // then
        assertThat(result).isEqualTo(expectedMembers);
        verify(myStudyService).getMyStudies(userId);
    }

    @Test
    @DisplayName("내 스터디 대시보드를 조회할 수 있다")
    void givenUserId_whenGetMyDashboard_thenReturnsDashboardMap() {
        // given
        final String userId = "user123";
        Map<String, Object> expectedDashboard = Map.of("totalStudies", 3);

        given(myStudyService.getMyDashboard(userId)).willReturn(expectedDashboard);

        // when
        Map<String, Object> result = myStudyUseCase.getMyDashboard(userId);

        // then
        assertThat(result).isEqualTo(expectedDashboard);
        verify(myStudyService).getMyDashboard(userId);
    }

    @Test
    @DisplayName("추천 스터디를 조회할 수 있다")
    void givenUserId_whenGetRecommendations_thenReturnsListOfRecommendations() {
        // given
        final String userId = "user123";
        List<Map<String, Object>> expectedRecommendations = List.of(Map.of("studyId", UUID.randomUUID().toString()));

        given(myStudyService.getRecommendations(userId)).willReturn(expectedRecommendations);

        // when
        List<Map<String, Object>> result = myStudyUseCase.getRecommendations(userId);

        // then
        assertThat(result).isEqualTo(expectedRecommendations);
        verify(myStudyService).getRecommendations(userId);
    }
}
