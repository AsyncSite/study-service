package com.asyncsite.studyservice.membership.domain.port.in;

import com.asyncsite.studyservice.membership.domain.model.Application;
import com.asyncsite.studyservice.membership.domain.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.UUID;

public interface ApplicationUseCase {
    Application apply(UUID studyId, String applicantId, Map<String, String> answers);
    Page<Application> getApplications(UUID studyId, Pageable pageable);
    Application getApplicationById(UUID applicationId);
    Member accept(UUID applicationId, String reviewerId, String note);
    void reject(UUID applicationId, String reviewerId, String reason);
    void cancelApplication(UUID applicationId, String applicantId);
}
