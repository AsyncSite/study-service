package com.asyncsite.studyservice.membership.domain.port.out;

import com.asyncsite.studyservice.membership.domain.model.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationRepository {
    Application save(Application application);
    Optional<Application> findById(UUID id);
    List<Application> findByStudyId(UUID studyId);
    Page<Application> findByStudyId(UUID studyId, Pageable pageable);
    List<Application> findByApplicantId(String applicantId);
    boolean existsByStudyIdAndApplicantIdAndStatus(UUID studyId, String applicantId);
    void deleteById(UUID id);
}