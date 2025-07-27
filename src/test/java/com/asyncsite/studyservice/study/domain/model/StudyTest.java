package com.asyncsite.studyservice.study.domain.model;

import com.asyncsite.studyservice.study.domain.exception.StudyAlreadyApprovedException;
import com.asyncsite.studyservice.study.domain.exception.StudyAlreadyRejectedException;
import com.asyncsite.studyservice.study.domain.exception.StudyAlreadyTerminatedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Study 도메인 모델 테스트")
class StudyTest {

    @Test
    @DisplayName("새로운 스터디 생성 시 기본값이 올바르게 설정된다")
    void givenValidParameters_whenCreateNewStudy_thenDefaultValuesAreSet() {
        // given
        final String title = "Spring Boot 스터디";
        final String description = "Spring Boot를 학습하는 스터디";
        final String proposerId = "user001";

        // when
        final Study study = new Study(title, description, proposerId);

        // then
        assertThat(study.getId()).isNotNull();
        assertThat(study.getTitle()).isEqualTo(title);
        assertThat(study.getDescription()).isEqualTo(description);
        assertThat(study.getProposerId()).isEqualTo(proposerId);
        assertThat(study.getStatus()).isEqualTo(StudyStatus.PENDING);
        assertThat(study.getCreatedAt()).isNotNull();
        assertThat(study.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("PENDING 상태의 스터디를 승인할 수 있다")
    void givenPendingStudy_whenApprove_thenStatusChangesToApproved() {
        // given
        final Study study = new Study("Test Study", "Description", "user001");
        final LocalDateTime beforeUpdate = study.getUpdatedAt();

        // when
        study.approve();

        // then
        assertThat(study.getStatus()).isEqualTo(StudyStatus.APPROVED);
        assertThat(study.getUpdatedAt()).isAfter(beforeUpdate);
    }

    @Test
    @DisplayName("PENDING 상태의 스터디를 거절할 수 있다")
    void givenPendingStudy_whenReject_thenStatusChangesToRejected() {
        // given
        final Study study = new Study("Test Study", "Description", "user001");
        final LocalDateTime beforeUpdate = study.getUpdatedAt();

        // when
        study.reject();

        // then
        assertThat(study.getStatus()).isEqualTo(StudyStatus.REJECTED);
        assertThat(study.getUpdatedAt()).isAfter(beforeUpdate);
    }

    @Test
    @DisplayName("APPROVED 상태의 스터디를 종료할 수 있다")
    void givenApprovedStudy_whenTerminate_thenStatusChangesToTerminated() {
        // given
        final Study study = new Study("Test Study", "Description", "user001");
        study.approve();
        final LocalDateTime beforeUpdate = study.getUpdatedAt();

        // when
        study.terminate();

        // then
        assertThat(study.getStatus()).isEqualTo(StudyStatus.TERMINATED);
        assertThat(study.getUpdatedAt()).isAfter(beforeUpdate);
    }

    @Test
    @DisplayName("이미 승인된 스터디를 다시 승인하려고 하면 예외가 발생한다")
    void givenApprovedStudy_whenApproveAgain_thenThrowsException() {
        // given
        final Study study = new Study("Test Study", "Description", "user001");
        study.approve();

        // when & then
        assertThatThrownBy(study::approve)
                .isInstanceOf(StudyAlreadyApprovedException.class);
    }

    @Test
    @DisplayName("이미 거절된 스터디를 다시 거절하려고 하면 예외가 발생한다")
    void givenRejectedStudy_whenRejectAgain_thenThrowsException() {
        // given
        final Study study = new Study("Test Study", "Description", "user001");
        study.reject();

        // when & then
        assertThatThrownBy(study::reject)
                .isInstanceOf(StudyAlreadyRejectedException.class);
    }

    @Test
    @DisplayName("이미 종료된 스터디를 다시 종료하려고 하면 예외가 발생한다")
    void givenTerminatedStudy_whenTerminateAgain_thenThrowsException() {
        // given
        final Study study = new Study("Test Study", "Description", "user001");
        study.approve();
        study.terminate();

        // when & then
        assertThatThrownBy(study::terminate)
                .isInstanceOf(StudyAlreadyTerminatedException.class);
    }

    @Test
    @DisplayName("모든 필드가 포함된 생성자로 스터디를 생성할 수 있다")
    void givenAllFields_whenCreateStudy_thenAllFieldsAreSet() {
        // given
        final UUID id = UUID.randomUUID();
        final String title = "Test Study";
        final String description = "Description";
        final String proposerId = "user001";
        final StudyStatus status = StudyStatus.APPROVED;
        final LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        final LocalDateTime updatedAt = LocalDateTime.now();

        // when
        final Study study = new Study(id, title, description, proposerId, status, createdAt, updatedAt);

        // then
        assertThat(study.getId()).isEqualTo(id);
        assertThat(study.getTitle()).isEqualTo(title);
        assertThat(study.getDescription()).isEqualTo(description);
        assertThat(study.getProposerId()).isEqualTo(proposerId);
        assertThat(study.getStatus()).isEqualTo(status);
        assertThat(study.getCreatedAt()).isEqualTo(createdAt);
        assertThat(study.getUpdatedAt()).isEqualTo(updatedAt);
    }
}