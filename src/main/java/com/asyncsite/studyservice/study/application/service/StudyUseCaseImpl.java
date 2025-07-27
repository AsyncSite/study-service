package com.asyncsite.studyservice.study.application.service;

import com.asyncsite.studyservice.study.domain.model.Study;
import com.asyncsite.studyservice.study.domain.port.in.GetStudyUseCase;
import com.asyncsite.studyservice.study.domain.port.in.ManageStudyUseCase;
import com.asyncsite.studyservice.study.domain.port.in.ProposeStudyUseCase;
import com.asyncsite.studyservice.study.domain.port.in.StudyUseCase;
import com.asyncsite.studyservice.study.domain.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyUseCaseImpl implements StudyUseCase, ProposeStudyUseCase, ManageStudyUseCase, GetStudyUseCase {
    private final StudyService studyService;

    @Override
    public Study propose(String title, String description, String proposerId) {
        return studyService.createStudy(title, description, proposerId);
    }

    @Override
    public Study approve(UUID studyId) {
        return studyService.approveStudy(studyId);
    }

    @Override
    public Study reject(UUID studyId) {
        return studyService.rejectStudy(studyId);
    }

    @Override
    public Study terminate(UUID studyId) {
        return studyService.terminateStudy(studyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Study> getAllStudies() {
        return studyService.getAllStudies();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Study> getAllStudies(Pageable pageable) {
        return studyService.getAllStudies(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Study> getStudyById(UUID studyId) {
        return studyService.getStudyById(studyId);
    }
}
