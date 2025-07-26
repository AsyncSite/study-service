package com.asyncsite.studyservice.membership.domain.port.in;

import com.asyncsite.studyservice.membership.domain.model.Application;

import java.util.Map;
import java.util.UUID;

public interface ApplyToStudyUseCase {
    Application apply(UUID studyId, String applicantId, Map<String, String> answers);
}