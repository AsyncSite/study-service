package com.asyncsite.studyservice.membership.domain.port.out;

import com.asyncsite.studyservice.domain.model.StudyStatus;

import java.util.UUID;

public interface StudyValidationPort {
    boolean isStudyExists(UUID studyId);
    boolean isStudyRecruiting(UUID studyId);
    StudyStatus getStudyStatus(UUID studyId);
    boolean isUserStudyLeader(UUID studyId, String userId);
}