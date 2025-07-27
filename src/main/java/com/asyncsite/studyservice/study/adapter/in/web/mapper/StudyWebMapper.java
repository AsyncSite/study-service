package com.asyncsite.studyservice.study.adapter.in.web.mapper;

import com.asyncsite.studyservice.study.adapter.in.web.StudyResponse;
import com.asyncsite.studyservice.study.domain.model.Study;
import org.springframework.stereotype.Component;

@Component
public class StudyWebMapper {

    public StudyResponse toResponse(final Study study) {
        return new StudyResponse(
                study.getId(),
                study.getTitle(),
                study.getDescription(),
                study.getProposerId(),
                study.getStatus(),
                study.getCreatedAt(),
                study.getUpdatedAt()
        );
    }
}
