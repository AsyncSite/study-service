package com.asyncsite.studyservice.study.domain.service;

import com.asyncsite.studyservice.study.domain.model.Study;
import com.asyncsite.studyservice.study.domain.port.out.NotificationPort;
import com.asyncsite.studyservice.study.domain.port.out.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class StudyService {
    private final StudyRepository studyRepository;
    private final NotificationPort notificationPort;

    public Study createStudy(String title, String description, String proposerId) {
        Study study = new Study(title, description, proposerId);
        Study savedStudy = studyRepository.save(study);
        notificationPort.sendStudyProposalNotification(savedStudy);
        return savedStudy;
    }

    public Study approveStudy(UUID studyId) {
        Study study = findStudyById(studyId);
        study.approve();
        Study savedStudy = studyRepository.save(study);
        notificationPort.sendStudyApprovalNotification(savedStudy);
        return savedStudy;
    }

    public Study rejectStudy(UUID studyId) {
        Study study = findStudyById(studyId);
        study.reject();
        Study savedStudy = studyRepository.save(study);
        notificationPort.sendStudyRejectionNotification(savedStudy);
        return savedStudy;
    }

    public Study terminateStudy(UUID studyId) {
        Study study = findStudyById(studyId);
        study.terminate();
        Study savedStudy = studyRepository.save(study);
        notificationPort.sendStudyTerminationNotification(savedStudy);
        return savedStudy;
    }

    public List<Study> getAllStudies() {
        return studyRepository.findAll();
    }
    
    public Page<Study> getAllStudies(Pageable pageable) {
        return studyRepository.findAll(pageable);
    }

    public Optional<Study> getStudyById(UUID studyId) {
        return studyRepository.findById(studyId);
    }

    private Study findStudyById(UUID studyId) {
        Optional<Study> studyOpt = studyRepository.findById(studyId);
        if (studyOpt.isEmpty()) {
            throw new StudyNotFoundException("Study not found with id: " + studyId);
        }
        return studyOpt.get();
    }
}
