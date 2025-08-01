package com.asyncsite.studyservice.membership.domain.port.in;

import com.asyncsite.studyservice.membership.domain.model.ApplicationForm;
import com.asyncsite.studyservice.membership.domain.model.ApplicationQuestion;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationFormUseCase {
    Optional<ApplicationForm> getFormByStudyId(UUID studyId);
    ApplicationForm createForm(UUID studyId, List<ApplicationQuestion> questions);
    ApplicationForm updateForm(UUID formId, List<ApplicationQuestion> questions);
    void deactivateForm(UUID formId);
}