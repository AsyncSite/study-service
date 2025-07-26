package com.asyncsite.studyservice.membership.adapter.out.persistence;

import com.asyncsite.studyservice.domain.model.Study;
import com.asyncsite.studyservice.domain.model.StudyStatus;
import com.asyncsite.studyservice.domain.port.out.StudyRepository;
import com.asyncsite.studyservice.membership.domain.port.out.StudyValidationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StudyValidationAdapter implements StudyValidationPort {
    
    private final StudyRepository studyRepository;
    
    @Override
    public boolean isStudyExists(UUID studyId) {
        return studyRepository.findById(studyId).isPresent();
    }
    
    @Override
    public boolean isStudyRecruiting(UUID studyId) {
        return studyRepository.findById(studyId)
                .map(study -> study.getStatus() == StudyStatus.APPROVED)
                .orElse(false);
    }
    
    @Override
    public StudyStatus getStudyStatus(UUID studyId) {
        return studyRepository.findById(studyId)
                .map(Study::getStatus)
                .orElse(null);
    }
    
    @Override
    public boolean isUserStudyLeader(UUID studyId, String userId) {
        // Mock implementation - 실제로는 스터디의 리더 정보를 확인해야 함
        return studyRepository.findById(studyId)
                .map(study -> study.getProposerId().equals(userId))
                .orElse(false);
    }
}