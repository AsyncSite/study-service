package com.asyncsite.studyservice.study.domain.port.in;

import com.asyncsite.studyservice.study.domain.model.Study;

import java.util.UUID;

public interface ManageStudyUseCase {
    Study approve(UUID studyId);
    Study reject(UUID studyId);
    Study terminate(UUID studyId);
}
