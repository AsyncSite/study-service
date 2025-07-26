package com.asyncsite.studyservice.membership.application.service;

import com.asyncsite.studyservice.membership.domain.model.ApplicationForm;
import com.asyncsite.studyservice.membership.domain.model.ApplicationQuestion;
import com.asyncsite.studyservice.membership.domain.port.in.ManageApplicationFormUseCase;
import com.asyncsite.studyservice.membership.domain.port.out.ApplicationFormRepository;
import com.asyncsite.studyservice.membership.domain.service.MembershipDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ManageApplicationFormService implements ManageApplicationFormUseCase {
    private final ApplicationFormRepository applicationFormRepository;
    private final MembershipDomainService membershipDomainService;
    
    @Override
    public ApplicationForm createForm(UUID studyId, List<ApplicationQuestion> questions) {
        // 기존 활성 폼이 있다면 비활성화
        applicationFormRepository.findByStudyIdAndActive(studyId, true)
                .ifPresent(form -> {
                    form.deactivate();
                    applicationFormRepository.save(form);
                });
        
        ApplicationForm form = ApplicationForm.create(studyId, questions);
        return applicationFormRepository.save(form);
    }
    
    @Override
    public ApplicationForm updateForm(UUID formId, List<ApplicationQuestion> questions) {
        ApplicationForm form = applicationFormRepository.findById(formId)
                .orElseThrow(() -> new IllegalArgumentException("Form not found: " + formId));
        
        form.updateQuestions(questions);
        return applicationFormRepository.save(form);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<ApplicationForm> getFormByStudyId(UUID studyId) {
        return applicationFormRepository.findByStudyIdAndActive(studyId, true);
    }
    
    @Override
    public void deactivateForm(UUID formId) {
        ApplicationForm form = applicationFormRepository.findById(formId)
                .orElseThrow(() -> new IllegalArgumentException("Form not found: " + formId));
        
        form.deactivate();
        applicationFormRepository.save(form);
    }
}