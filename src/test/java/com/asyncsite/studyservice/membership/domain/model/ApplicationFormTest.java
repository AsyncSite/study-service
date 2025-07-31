package com.asyncsite.studyservice.membership.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ApplicationForm 도메인 모델 테스트")
class ApplicationFormTest {

    @Test
    @DisplayName("ApplicationForm 객체를 생성할 수 있다")
    void createApplicationForm() {
        UUID studyId = UUID.randomUUID();
        String title = "Test Form";
        String description = "Test Description";
        String createdBy = "testUser";

        ApplicationForm form = ApplicationForm.create(studyId, title, description, createdBy);

        assertThat(form.getId()).isNotNull();
        assertThat(form.getStudyId()).isEqualTo(studyId);
        assertThat(form.getTitle()).isEqualTo(title);
        assertThat(form.getDescription()).isEqualTo(description);
        assertThat(form.getQuestions()).isEmpty();
        assertThat(form.isActive()).isTrue();
        assertThat(form.getCreatedAt()).isNotNull();
        assertThat(form.getUpdatedAt()).isNotNull();
        assertThat(form.getCreatedBy()).isEqualTo(createdBy);
    }

    @Test
    @DisplayName("질문 목록으로 ApplicationForm 객체를 생성할 수 있다")
    void createApplicationFormWithQuestions() {
        UUID studyId = UUID.randomUUID();
        List<ApplicationQuestion> questions = List.of(
                ApplicationQuestion.builder().question("Q1").type(ApplicationQuestion.QuestionType.TEXT).required(true).build()
        );

        ApplicationForm form = ApplicationForm.create(studyId, questions);

        assertThat(form.getId()).isNotNull();
        assertThat(form.getStudyId()).isEqualTo(studyId);
        assertThat(form.getTitle()).isEqualTo("스터디 지원서");
        assertThat(form.getDescription()).isEqualTo("스터디 참여를 위한 지원서입니다.");
        assertThat(form.getQuestions()).isEqualTo(questions);
        assertThat(form.isActive()).isTrue();
        assertThat(form.getCreatedAt()).isNotNull();
        assertThat(form.getUpdatedAt()).isNotNull();
        assertThat(form.getCreatedBy()).isEqualTo("system");
    }

    @Test
    @DisplayName("지원서 양식을 활성화할 수 있다")
    void activateApplicationForm() {
        ApplicationForm form = ApplicationForm.create(UUID.randomUUID(), "", "", "");
        form.deactivate(); // Initially deactivate for testing activation

        form.activate();

        assertThat(form.isActive()).isTrue();
        assertThat(form.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("지원서 양식을 비활성화할 수 있다")
    void deactivateApplicationForm() {
        ApplicationForm form = ApplicationForm.create(UUID.randomUUID(), "", "", "");

        form.deactivate();

        assertThat(form.isActive()).isFalse();
        assertThat(form.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("질문을 추가할 수 있다")
    void addQuestion() {
        ApplicationForm form = ApplicationForm.create(UUID.randomUUID(), "", "", "");
        ApplicationQuestion question = ApplicationQuestion.builder().question("New Q").build();

        form.addQuestion(question);

        assertThat(form.getQuestions()).containsExactly(question);
        assertThat(form.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("질문을 제거할 수 있다")
    void removeQuestion() {
        ApplicationQuestion q1 = ApplicationQuestion.builder().question("Q1").build();
        ApplicationQuestion q2 = ApplicationQuestion.builder().question("Q2").build();
        ApplicationForm form = ApplicationForm.create(UUID.randomUUID(), List.of(q1, q2));

        form.removeQuestion(0);

        assertThat(form.getQuestions()).containsExactly(q2);
        assertThat(form.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("질문을 수정할 수 있다")
    void updateQuestion() {
        ApplicationQuestion q1 = ApplicationQuestion.builder().question("Q1").build();
        ApplicationForm form = ApplicationForm.create(UUID.randomUUID(), List.of(q1));
        ApplicationQuestion updatedQ1 = ApplicationQuestion.builder().question("Updated Q1").build();

        form.updateQuestion(0, updatedQ1);

        assertThat(form.getQuestions()).containsExactly(updatedQ1);
        assertThat(form.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("질문 목록을 업데이트할 수 있다")
    void updateQuestions() {
        ApplicationForm form = ApplicationForm.create(UUID.randomUUID(), "", "", "");
        List<ApplicationQuestion> newQuestions = List.of(
                ApplicationQuestion.builder().question("New Q1").build(),
                ApplicationQuestion.builder().question("New Q2").build()
        );

        form.updateQuestions(newQuestions);

        assertThat(form.getQuestions()).isEqualTo(newQuestions);
        assertThat(form.getUpdatedAt()).isNotNull();
    }
}
