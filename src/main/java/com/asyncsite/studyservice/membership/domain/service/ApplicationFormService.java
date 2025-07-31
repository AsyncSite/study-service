package com.asyncsite.studyservice.membership.domain.service;

import com.asyncsite.studyservice.membership.domain.model.ApplicationForm;
import com.asyncsite.studyservice.membership.domain.model.ApplicationQuestion;
import com.asyncsite.studyservice.membership.domain.port.out.ApplicationFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationFormService {
    private final ApplicationFormRepository applicationFormRepository;

    @Transactional(readOnly = true)
    public Optional<ApplicationForm> getFormByStudyId(UUID studyId) {
        return applicationFormRepository.findByStudyIdAndActive(studyId, true);
    }

    public ApplicationForm createForm(UUID studyId, List<ApplicationQuestion> questions) {
        // 기존 활성 폼 비활성화
        applicationFormRepository.findByStudyIdAndActive(studyId, true)
                .ifPresent(form -> {
                    form.deactivate();
                    applicationFormRepository.save(form);
                });

        ApplicationForm newForm = ApplicationForm.create(studyId, questions);
        return applicationFormRepository.save(newForm);
    }

    public ApplicationForm updateForm(UUID formId, List<ApplicationQuestion> questions) {
        ApplicationForm form = applicationFormRepository.findById(formId)
                .orElseThrow(() -> new IllegalArgumentException("Application form not found: " + formId));
        form.updateQuestions(questions);
        return applicationFormRepository.save(form);
    }

    public void deactivateForm(UUID formId) {
        ApplicationForm form = applicationFormRepository.findById(formId)
                .orElseThrow(() -> new IllegalArgumentException("Application form not found: " + formId));
        form.deactivate();
        applicationFormRepository.save(form);
    }
}