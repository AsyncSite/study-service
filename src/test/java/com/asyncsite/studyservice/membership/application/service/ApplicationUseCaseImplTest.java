package com.asyncsite.studyservice.membership.application.service;

import com.asyncsite.studyservice.membership.domain.model.Application;
import com.asyncsite.studyservice.membership.domain.model.ApplicationForm;
import com.asyncsite.studyservice.membership.domain.model.ApplicationQuestion;
import com.asyncsite.studyservice.membership.domain.model.Member;
import com.asyncsite.studyservice.membership.domain.port.in.ApplicationFormUseCase;
import com.asyncsite.studyservice.membership.domain.port.in.ApplicationUseCase;
import com.asyncsite.studyservice.membership.domain.service.ApplicationFormService;
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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApplicationUseCaseImpl 테스트")
class ApplicationUseCaseImplTest {

    @Mock
    private ApplicationService applicationService;
    @Mock
    private ApplicationFormService applicationFormService;

    private ApplicationUseCase applicationUseCase;
    private ApplicationFormUseCase applicationFormUseCase;

    @BeforeEach
    void setUp() {
        applicationUseCase = new ApplicationUseCaseImpl(applicationService, applicationFormService);
        applicationFormUseCase = (ApplicationFormUseCase) applicationUseCase;
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

    @Test
    @DisplayName("스터디 ID로 지원서 양식을 조회할 수 있다")
    void givenStudyId_whenGetFormByStudyId_thenReturnsApplicationForm() {
        // given
        final UUID studyId = UUID.randomUUID();
        ApplicationForm expectedForm = ApplicationForm.builder().build();

        given(applicationFormService.getFormByStudyId(studyId)).willReturn(Optional.of(expectedForm));

        // when
        Optional<ApplicationForm> result = applicationFormUseCase.getFormByStudyId(studyId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expectedForm);
        verify(applicationFormService).getFormByStudyId(studyId);
    }

    @Test
    @DisplayName("지원서 양식을 생성할 수 있다")
    void givenStudyIdAndQuestions_whenCreateForm_thenReturnsCreatedApplicationForm() {
        // given
        final UUID studyId = UUID.randomUUID();
        final List<ApplicationQuestion> questions = List.of(ApplicationQuestion.builder().build());
        ApplicationForm expectedForm = ApplicationForm.builder().build();

        given(applicationFormService.createForm(studyId, questions)).willReturn(expectedForm);

        // when
        ApplicationForm result = applicationFormUseCase.createForm(studyId, questions);

        // then
        assertThat(result).isEqualTo(expectedForm);
        verify(applicationFormService).createForm(studyId, questions);
    }

    @Test
    @DisplayName("지원서 양식을 수정할 수 있다")
    void givenFormIdAndQuestions_whenUpdateForm_thenReturnsUpdatedApplicationForm() {
        // given
        final UUID formId = UUID.randomUUID();
        final List<ApplicationQuestion> questions = List.of(ApplicationQuestion.builder().build());
        ApplicationForm expectedForm = ApplicationForm.builder().build();

        given(applicationFormService.updateForm(formId, questions)).willReturn(expectedForm);

        // when
        ApplicationForm result = applicationFormUseCase.updateForm(formId, questions);

        // then
        assertThat(result).isEqualTo(expectedForm);
        verify(applicationFormService).updateForm(formId, questions);
    }

    @Test
    @DisplayName("지원서 양식을 비활성화할 수 있다")
    void givenFormId_whenDeactivateForm_thenCallsApplicationFormServiceDeactivateForm() {
        // given
        final UUID formId = UUID.randomUUID();

        // when
        applicationFormUseCase.deactivateForm(formId);

        // then
        verify(applicationFormService).deactivateForm(formId);
    }
}
