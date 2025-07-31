package com.asyncsite.studyservice.membership.application.service;

import com.asyncsite.studyservice.membership.domain.model.Application;
import com.asyncsite.studyservice.membership.domain.model.Member;
import com.asyncsite.studyservice.membership.domain.port.in.ApplicationUseCase;
import com.asyncsite.studyservice.membership.domain.service.ApplicationService;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApplicationUseCaseImpl 테스트")
class ApplicationUseCaseImplTest {

    @Mock
    private ApplicationService applicationService;

    private ApplicationUseCase applicationUseCase;

    @BeforeEach
    void setUp() {
        applicationUseCase = new ApplicationUseCaseImpl(applicationService);
    }

    @Test
    @DisplayName("스터디에 지원할 수 있다")
    void givenValidApplicationInfo_whenApply_thenReturnsCreatedApplication() {
        // given
        final UUID studyId = UUID.randomUUID();
        final String applicantId = "user-test-id";
        final Map<String, String> answers = Map.of("질문1", "답변1");
        Application expectedApplication = Application.builder().build();

        given(applicationService.apply(studyId, applicantId, answers))
                .willReturn(expectedApplication);

        // when
        Application result = applicationUseCase.apply(studyId, applicantId, answers);

        // then
        assertThat(result).isEqualTo(expectedApplication);
        verify(applicationService).apply(studyId, applicantId, answers);
    }

    @Test
    @DisplayName("스터디 지원 목록을 조회할 수 있다")
    void givenStudyId_whenGetApplications_thenReturnsApplicationPage() {
        // given
        final UUID studyId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);
        List<Application> applications = List.of(Application.builder().build());
        Page<Application> expectedPage = new PageImpl<>(applications, pageable, applications.size());

        given(applicationService.getApplications(studyId, pageable)).willReturn(expectedPage);

        // when
        Page<Application> result = applicationUseCase.getApplications(studyId, pageable);

        // then
        assertThat(result).isEqualTo(expectedPage);
        verify(applicationService).getApplications(studyId, pageable);
    }

    @Test
    @DisplayName("ID로 지원서를 조회할 수 있다")
    void givenApplicationId_whenGetApplicationById_thenReturnsApplication() {
        // given
        final UUID applicationId = UUID.randomUUID();
        Application expectedApplication = Application.builder().id(applicationId).build();

        given(applicationService.getApplicationById(applicationId)).willReturn(expectedApplication);

        // when
        Application result = applicationUseCase.getApplicationById(applicationId);

        // then
        assertThat(result).isEqualTo(expectedApplication);
        verify(applicationService).getApplicationById(applicationId);
    }

    @Test
    @DisplayName("지원서를 승인할 수 있다")
    void givenApplicationIdAndReviewer_whenAccept_thenReturnsMember() {
        // given
        final UUID applicationId = UUID.randomUUID();
        final String reviewerId = "reviewer-id";
        final String note = "Welcome!";
        Member expectedMember = Member.builder().build();

        given(applicationService.accept(applicationId, reviewerId, note)).willReturn(expectedMember);

        // when
        Member result = applicationUseCase.accept(applicationId, reviewerId, note);

        // then
        assertThat(result).isEqualTo(expectedMember);
        verify(applicationService).accept(applicationId, reviewerId, note);
    }

    @Test
    @DisplayName("지원서를 거절할 수 있다")
    void givenApplicationIdAndReviewer_whenReject_thenCallsApplicationServiceReject() {
        // given
        final UUID applicationId = UUID.randomUUID();
        final String reviewerId = "reviewer-id";
        final String reason = "Not a good fit.";

        // when
        applicationUseCase.reject(applicationId, reviewerId, reason);

        // then
        verify(applicationService).reject(applicationId, reviewerId, reason);
    }

    @Test
    @DisplayName("지원서를 취소할 수 있다")
    void givenApplicationIdAndApplicant_whenCancelApplication_thenCallsApplicationServiceCancelApplication() {
        // given
        final UUID applicationId = UUID.randomUUID();
        final String applicantId = "applicant-id";

        // when
        applicationUseCase.cancelApplication(applicationId, applicantId);

        // then
        verify(applicationService).cancelApplication(applicationId, applicantId);
    }
}
