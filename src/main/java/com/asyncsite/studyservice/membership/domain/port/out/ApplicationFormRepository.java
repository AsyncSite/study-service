package com.asyncsite.studyservice.membership.domain.port.out;

import com.asyncsite.studyservice.membership.domain.model.ApplicationForm;

import java.util.Optional;
import java.util.UUID;

public interface ApplicationFormRepository {
    ApplicationForm save(ApplicationForm form);
    Optional<ApplicationForm> findById(UUID id);
    Optional<ApplicationForm> findByStudyIdAndActive(UUID studyId, boolean isActive);
    void deleteById(UUID id);
}