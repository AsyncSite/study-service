package com.asyncsite.studyservice.membership.domain.port.in;

import com.asyncsite.studyservice.membership.domain.model.ApplicationForm;
import com.asyncsite.studyservice.membership.domain.model.ApplicationQuestion;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ManageApplicationFormUseCase {
    ApplicationForm createForm(UUID studyId, List<ApplicationQuestion> questions);
    ApplicationForm updateForm(UUID formId, List<ApplicationQuestion> questions);
    Optional<ApplicationForm> getFormByStudyId(UUID studyId);
    void deactivateForm(UUID formId);
}