package com.asyncsite.studyservice.membership.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Application 도메인 모델 테스트")
class ApplicationTest {

    @Test
    @DisplayName("Application 객체를 생성할 수 있다")
    void createApplication() {
        UUID studyId = UUID.randomUUID();
        String applicantId = "user1";
        Map<String, String> answers = Map.of("q1", "a1");
        String introduction = "intro";

        Application application = Application.create(studyId, "Test Study Title", applicantId, answers, introduction);

        assertThat(application.getId()).isNotNull();
        assertThat(application.getStudyId()).isEqualTo(studyId);
        assertThat(application.getApplicantId()).isEqualTo(applicantId);
        assertThat(application.getAnswers()).isEqualTo(answers);
        assertThat(application.getIntroduction()).isEqualTo(introduction);
        assertThat(application.getStatus()).isEqualTo(ApplicationStatus.PENDING);
        assertThat(application.getAppliedAt()).isNotNull();
        assertThat(application.getCreatedAt()).isNotNull();
        assertThat(application.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("지원서를 승인할 수 있다")
    void acceptApplication() {
        Application application = Application.create(UUID.randomUUID(), "Test Study Title", "user1", Map.of(), "");
        String acceptedBy = "admin1";
        String note = "Good fit";

        application.accept(acceptedBy, note);

        assertThat(application.getStatus()).isEqualTo(ApplicationStatus.ACCEPTED);
        assertThat(application.getProcessedAt()).isNotNull();
        assertThat(application.getProcessedBy()).isEqualTo(acceptedBy);
        assertThat(application.getReviewNote()).isEqualTo(note);
        assertThat(application.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("이미 처리된 지원서는 승인할 수 없다")
    void acceptProcessedApplicationThrowsException() {
        Application application = Application.create(UUID.randomUUID(), "Test Study Title", "user1", Map.of(), "");
        application.accept("admin1");

        assertThatThrownBy(() -> application.accept("admin2"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 처리된 지원서입니다.");
    }

    @Test
    @DisplayName("지원서를 거절할 수 있다")
    void rejectApplication() {
        Application application = Application.create(UUID.randomUUID(), "Test Study Title", "user1", Map.of(), "");
        String rejectedBy = "admin1";
        String reason = "Not suitable";

        application.reject(rejectedBy, reason);

        assertThat(application.getStatus()).isEqualTo(ApplicationStatus.REJECTED);
        assertThat(application.getRejectionReason()).isEqualTo(reason);
        assertThat(application.getProcessedAt()).isNotNull();
        assertThat(application.getProcessedBy()).isEqualTo(rejectedBy);
    }

    @Test
    @DisplayName("이미 처리된 지원서는 거절할 수 없다")
    void rejectProcessedApplicationThrowsException() {
        Application application = Application.create(UUID.randomUUID(), "Test Study Title", "user1", Map.of(), "");
        application.reject("admin1", "");

        assertThatThrownBy(() -> application.reject("admin2", ""))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 처리된 지원서입니다.");
    }

    @Test
    @DisplayName("지원서를 취소할 수 있다")
    void cancelApplication() {
        Application application = Application.create(UUID.randomUUID(), "Test Study Title", "user1", Map.of(), "");

        application.cancel();

        assertThat(application.getStatus()).isEqualTo(ApplicationStatus.CANCELLED);
        assertThat(application.getProcessedAt()).isNotNull();
    }

    @Test
    @DisplayName("처리된 지원서는 취소할 수 없다")
    void cancelProcessedApplicationThrowsException() {
        Application application = Application.create(UUID.randomUUID(), "Test Study Title", "user1", Map.of(), "");
        application.accept("admin1");

        assertThatThrownBy(application::cancel)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("대기 중인 지원서만 취소할 수 있습니다.");
    }

    @Test
    @DisplayName("지원서가 대기 중인지 확인할 수 있다")
    void isPending() {
        Application pendingApp = Application.create(UUID.randomUUID(), "Test Study Title", "user1", Map.of(), "");
        assertThat(pendingApp.isPending()).isTrue();

        Application acceptedApp = Application.create(UUID.randomUUID(), "Test Study Title", "user1", Map.of(), "");
        acceptedApp.accept("admin1");
        assertThat(acceptedApp.isPending()).isFalse();
    }

    @Test
    @DisplayName("지원서가 처리되었는지 확인할 수 있다")
    void isProcessed() {
        Application pendingApp = Application.create(UUID.randomUUID(), "Test Study Title", "user1", Map.of(), "");
        assertThat(pendingApp.isProcessed()).isFalse();

        Application acceptedApp = Application.create(UUID.randomUUID(), "Test Study Title", "user1", Map.of(), "");
        acceptedApp.accept("admin1");
        assertThat(acceptedApp.isProcessed()).isTrue();

        Application rejectedApp = Application.create(UUID.randomUUID(), "Test Study Title", "user1", Map.of(), "");
        rejectedApp.reject("admin1", "");
        assertThat(rejectedApp.isProcessed()).isTrue();
    }
}
