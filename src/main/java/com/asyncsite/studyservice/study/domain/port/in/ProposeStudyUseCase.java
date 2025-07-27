package com.asyncsite.studyservice.study.domain.port.in;

import com.asyncsite.studyservice.study.domain.model.Study;

public interface ProposeStudyUseCase {
    Study propose(String title, String description, String proposerId);
}
