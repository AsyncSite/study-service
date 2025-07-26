package com.asyncsite.studyservice.membership.domain.port.in;

import com.asyncsite.studyservice.membership.domain.model.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface QueryApplicationUseCase {
    Page<Application> getApplicationsByStudyId(UUID studyId, Pageable pageable);
    Optional<Application> getApplicationById(UUID applicationId);
    void cancelApplication(UUID applicationId, String applicantId);
}