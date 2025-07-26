package com.asyncsite.studyservice.domain.port.in;

import com.asyncsite.studyservice.domain.model.Study;

public interface ProposeStudyUseCase {
    Study propose(String title, String description, String proposerId);
}