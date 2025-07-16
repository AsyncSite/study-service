package com.asyncsite.studyservice.domain.port.in;

import com.asyncsite.studyservice.domain.model.Study;

import java.util.UUID;

public interface ManageStudyUseCase {
    Study approve(UUID studyId);
    Study reject(UUID studyId);
    Study terminate(UUID studyId);
}