package com.asyncsite.studyservice.membership.domain.service;

import com.asyncsite.studyservice.membership.domain.exception.ApplicationNotFoundException;
import com.asyncsite.studyservice.membership.domain.exception.DuplicateApplicationException;
import com.asyncsite.studyservice.membership.domain.model.Application;
import com.asyncsite.studyservice.membership.domain.model.Member;
import com.asyncsite.studyservice.membership.domain.port.out.ApplicationRepository;
import com.asyncsite.studyservice.membership.domain.port.out.MemberRepository;
import com.asyncsite.studyservice.membership.domain.port.out.MembershipNotificationPort;
import com.asyncsite.studyservice.study.domain.port.out.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final StudyRepository studyRepository;
    private final MembershipNotificationPort notificationPort;
    private final MemberRepository memberRepository;
    private final MembershipDomainService membershipDomainService;

    public Application apply(UUID studyId, String applicantId, Map<String, String> answers) {
        if (!studyRepository.isStudyExists(studyId)) throw new IllegalArgumentException("Study not found: " + studyId);
        if (!studyRepository.isStudyRecruiting(studyId)) throw new IllegalStateException("Study is not recruiting");
        if (applicationRepository.existsByStudyIdAndApplicantIdAndStatus(studyId, applicantId)) throw new DuplicateApplicationException("Already applied to this study");

        String studyTitle = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("Study not found: " + studyId))
                .getTitle();

        Application savedApplication = applicationRepository.save(
                Application.create(studyId, studyTitle, applicantId, answers, "지원서 제출")
        );
        notificationPort.sendApplicationSubmittedNotification(savedApplication);
        return savedApplication;
    }

    public Page<Application> getApplications(UUID studyId, Pageable pageable) {
        return applicationRepository.findByStudyId(studyId, pageable);
    }

    public Application getApplicationById(UUID applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException("Application not found: " + applicationId));
    }

    public Member accept(UUID applicationId, String reviewerId, String note) {
        Application application = getApplicationById(applicationId);

        membershipDomainService.validateMemberManagementAuthority(application.getStudyId(), reviewerId);

        application.accept(reviewerId, note);
        applicationRepository.save(application);

        Member member = Member.createFromApplication(application);
        Member savedMember = memberRepository.save(member);

        notificationPort.sendApplicationAcceptedNotification(application);
        notificationPort.sendMemberJoinedNotification(savedMember);

        return savedMember;
    }

    public void reject(UUID applicationId, String reviewerId, String reason) {
        Application application = getApplicationById(applicationId);

        membershipDomainService.validateMemberManagementAuthority(application.getStudyId(), reviewerId);

        application.reject(reviewerId, reason);
        applicationRepository.save(application);

        notificationPort.sendApplicationRejectedNotification(application);
    }

    public void cancelApplication(UUID applicationId, String applicantId) {
        Application application = getApplicationById(applicationId);

        if (!application.getApplicantId().equals(applicantId)) {
            throw new IllegalArgumentException("Only the applicant can cancel their application.");
        }

        application.cancel();
        applicationRepository.save(application);
        notificationPort.sendApplicationCancelledNotification(application);
    }
}