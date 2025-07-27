package com.asyncsite.studyservice.study.adapter.out.persistence;

import com.asyncsite.studyservice.study.adapter.out.persistence.mapper.StudyPersistenceMapper;
import com.asyncsite.studyservice.study.adapter.out.persistence.repository.StudyJpaRepository;
import com.asyncsite.studyservice.study.domain.model.Study;
import com.asyncsite.studyservice.study.domain.model.StudyStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({StudyPersistenceAdapter.class, StudyPersistenceMapper.class})
@ActiveProfiles("test")
@DisplayName("StudyPersistenceAdapter 테스트")
class StudyPersistenceAdapterTest {

    @Autowired
    private StudyPersistenceAdapter persistenceAdapter;

    @Autowired
    private StudyJpaRepository jpaRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("새로운 스터디를 저장할 수 있다")
    void givenNewStudy_whenSave_thenStudyIsSaved() {
        // given
        final Study study = new Study("Spring Boot 스터디", "Spring Boot 학습", "user001");

        // when
        final Study savedStudy = persistenceAdapter.save(study);

        // then
        assertThat(savedStudy.getId()).isEqualTo(study.getId());
        assertThat(savedStudy.getTitle()).isEqualTo(study.getTitle());
        assertThat(savedStudy.getDescription()).isEqualTo(study.getDescription());
        assertThat(savedStudy.getProposerId()).isEqualTo(study.getProposerId());
        assertThat(savedStudy.getStatus()).isEqualTo(StudyStatus.PENDING);
    }

    @Test
    @DisplayName("기존 스터디를 업데이트할 수 있다")
    void givenExistingStudy_whenSave_thenStudyIsUpdated() {
        // given
        final Study study = new Study("Spring Boot 스터디", "Spring Boot 학습", "user001");
        final Study savedStudy = persistenceAdapter.save(study);
        
        // 상태 변경
        savedStudy.approve();

        // when
        final Study updatedStudy = persistenceAdapter.save(savedStudy);

        // then
        assertThat(updatedStudy.getId()).isEqualTo(savedStudy.getId());
        assertThat(updatedStudy.getStatus()).isEqualTo(StudyStatus.APPROVED);
        assertThat(updatedStudy.getUpdatedAt()).isAfter(savedStudy.getCreatedAt());
    }

    @Test
    @DisplayName("ID로 스터디를 조회할 수 있다")
    void givenStudyId_whenFindById_thenReturnsStudy() {
        // given
        final Study study = new Study("React 스터디", "React 학습", "user002");
        final Study savedStudy = persistenceAdapter.save(study);

        // when
        final Optional<Study> foundStudy = persistenceAdapter.findById(savedStudy.getId());

        // then
        assertThat(foundStudy).isPresent();
        assertThat(foundStudy.get().getId()).isEqualTo(savedStudy.getId());
        assertThat(foundStudy.get().getTitle()).isEqualTo("React 스터디");
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회하면 빈 Optional을 반환한다")
    void givenNonExistentId_whenFindById_thenReturnsEmpty() {
        // given
        final UUID nonExistentId = UUID.randomUUID();

        // when
        final Optional<Study> foundStudy = persistenceAdapter.findById(nonExistentId);

        // then
        assertThat(foundStudy).isEmpty();
    }

    @Test
    @DisplayName("모든 스터디를 조회할 수 있다")
    void givenMultipleStudies_whenFindAll_thenReturnsAllStudies() {
        // given
        final Study study1 = new Study("Spring 스터디", "Spring 학습", "user001");
        final Study study2 = new Study("React 스터디", "React 학습", "user002");
        persistenceAdapter.save(study1);
        persistenceAdapter.save(study2);

        // when
        final List<Study> allStudies = persistenceAdapter.findAll();

        // then
        assertThat(allStudies).hasSize(2);
        assertThat(allStudies).extracting(Study::getTitle)
                .containsExactlyInAnyOrder("Spring 스터디", "React 스터디");
    }

    @Test
    @DisplayName("페이징으로 스터디를 조회할 수 있다")
    void givenMultipleStudies_whenFindAllWithPaging_thenReturnsPagedResults() {
        // given
        for (int i = 1; i <= 5; i++) {
            final Study study = new Study("스터디 " + i, "설명 " + i, "user" + i);
            persistenceAdapter.save(study);
        }

        // when
        final Page<Study> firstPage = persistenceAdapter.findAll(PageRequest.of(0, 2));
        final Page<Study> secondPage = persistenceAdapter.findAll(PageRequest.of(1, 2));

        // then
        assertThat(firstPage.getContent()).hasSize(2);
        assertThat(firstPage.getTotalElements()).isEqualTo(5);
        assertThat(firstPage.getTotalPages()).isEqualTo(3);
        assertThat(firstPage.isFirst()).isTrue();
        assertThat(firstPage.hasNext()).isTrue();

        assertThat(secondPage.getContent()).hasSize(2);
        assertThat(secondPage.isFirst()).isFalse();
        assertThat(secondPage.hasNext()).isTrue();
    }

    @Test
    @DisplayName("스터디를 삭제할 수 있다")
    void givenExistingStudy_whenDeleteById_thenStudyIsDeleted() {
        // given
        final Study study = new Study("삭제할 스터디", "삭제 테스트", "user001");
        final Study savedStudy = persistenceAdapter.save(study);

        // when
        persistenceAdapter.deleteById(savedStudy.getId());

        // then
        final Optional<Study> deletedStudy = persistenceAdapter.findById(savedStudy.getId());
        assertThat(deletedStudy).isEmpty();
    }
}