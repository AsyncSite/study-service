package com.asyncsite.studyservice.study.application.service;

import com.asyncsite.studyservice.study.domain.model.Study;
import com.asyncsite.studyservice.study.domain.model.StudyStatus;
import com.asyncsite.studyservice.study.domain.service.StudyService;
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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StudyUseCaseImpl 테스트")
class StudyUseCaseImplTest {

    @Mock
    private StudyService studyService;

    private StudyUseCaseImpl studyUseCase;

    @BeforeEach
    void setUp() {
        studyUseCase = new StudyUseCaseImpl(studyService);
    }

    @Test
    @DisplayName("스터디를 제안할 수 있다")
    void givenValidStudyInfo_whenPropose_thenReturnsCreatedStudy() {
        // given
        final String title = "Spring Boot 스터디";
        final String description = "Spring Boot 학습";
        final String proposerId = "user001";
        final Study expectedStudy = new Study(title, description, proposerId);

        given(studyService.createStudy(title, description, proposerId))
                .willReturn(expectedStudy);

        // when
        final Study result = studyUseCase.propose(title, description, proposerId);

        // then
        assertThat(result.getTitle()).isEqualTo(title);
        assertThat(result.getDescription()).isEqualTo(description);
        assertThat(result.getProposerId()).isEqualTo(proposerId);
        assertThat(result.getStatus()).isEqualTo(StudyStatus.PENDING);
        verify(studyService).createStudy(title, description, proposerId);
    }

    @Test
    @DisplayName("스터디를 승인할 수 있다")
    void givenStudyId_whenApprove_thenReturnsApprovedStudy() {
        // given
        final UUID studyId = UUID.randomUUID();
        final Study approvedStudy = new Study("Test Study", "Description", "user001");
        approvedStudy.approve();

        given(studyService.approveStudy(studyId))
                .willReturn(approvedStudy);

        // when
        final Study result = studyUseCase.approve(studyId);

        // then
        assertThat(result.getStatus()).isEqualTo(StudyStatus.APPROVED);
        verify(studyService).approveStudy(studyId);
    }

    @Test
    @DisplayName("스터디를 거절할 수 있다")
    void givenStudyId_whenReject_thenReturnsRejectedStudy() {
        // given
        final UUID studyId = UUID.randomUUID();
        final Study rejectedStudy = new Study("Test Study", "Description", "user001");
        rejectedStudy.reject();

        given(studyService.rejectStudy(studyId))
                .willReturn(rejectedStudy);

        // when
        final Study result = studyUseCase.reject(studyId);

        // then
        assertThat(result.getStatus()).isEqualTo(StudyStatus.REJECTED);
        verify(studyService).rejectStudy(studyId);
    }

    @Test
    @DisplayName("스터디를 종료할 수 있다")
    void givenStudyId_whenTerminate_thenReturnsTerminatedStudy() {
        // given
        final UUID studyId = UUID.randomUUID();
        final Study terminatedStudy = new Study("Test Study", "Description", "user001");
        terminatedStudy.approve();
        terminatedStudy.terminate();

        given(studyService.terminateStudy(studyId))
                .willReturn(terminatedStudy);

        // when
        final Study result = studyUseCase.terminate(studyId);

        // then
        assertThat(result.getStatus()).isEqualTo(StudyStatus.TERMINATED);
        verify(studyService).terminateStudy(studyId);
    }

    @Test
    @DisplayName("모든 스터디를 조회할 수 있다")
    void whenGetAllStudies_thenReturnsAllStudies() {
        // given
        final List<Study> expectedStudies = List.of(
                new Study("Study 1", "Description 1", "user001"),
                new Study("Study 2", "Description 2", "user002")
        );

        given(studyService.getAllStudies())
                .willReturn(expectedStudies);

        // when
        final List<Study> result = studyUseCase.getAllStudies();

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Study::getTitle)
                .containsExactly("Study 1", "Study 2");
        verify(studyService).getAllStudies();
    }

    @Test
    @DisplayName("페이징으로 스터디를 조회할 수 있다")
    void givenPageable_whenGetAllStudies_thenReturnsPagedStudies() {
        // given
        final Pageable pageable = PageRequest.of(0, 10);
        final List<Study> studies = List.of(
                new Study("Study 1", "Description 1", "user001"),
                new Study("Study 2", "Description 2", "user002")
        );
        final Page<Study> expectedPage = new PageImpl<>(studies, pageable, studies.size());

        given(studyService.getAllStudies(pageable))
                .willReturn(expectedPage);

        // when
        final Page<Study> result = studyUseCase.getAllStudies(pageable);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getPageable()).isEqualTo(pageable);
        verify(studyService).getAllStudies(pageable);
    }

    @Test
    @DisplayName("ID로 스터디를 조회할 수 있다")
    void givenStudyId_whenGetStudyById_thenReturnsStudy() {
        // given
        final UUID studyId = UUID.randomUUID();
        final Study expectedStudy = new Study("Test Study", "Description", "user001");

        given(studyService.getStudyById(studyId))
                .willReturn(Optional.of(expectedStudy));

        // when
        final Optional<Study> result = studyUseCase.getStudyById(studyId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Test Study");
        verify(studyService).getStudyById(studyId);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회하면 빈 Optional을 반환한다")
    void givenNonExistentId_whenGetStudyById_thenReturnsEmpty() {
        // given
        final UUID nonExistentId = UUID.randomUUID();

        given(studyService.getStudyById(nonExistentId))
                .willReturn(Optional.empty());

        // when
        final Optional<Study> result = studyUseCase.getStudyById(nonExistentId);

        // then
        assertThat(result).isEmpty();
        verify(studyService).getStudyById(nonExistentId);
    }
}