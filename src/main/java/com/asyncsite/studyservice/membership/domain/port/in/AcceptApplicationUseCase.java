package com.asyncsite.studyservice.membership.domain.port.in;

import com.asyncsite.studyservice.membership.domain.model.Member;

import java.util.UUID;

public interface AcceptApplicationUseCase {
    Member accept(UUID applicationId, String reviewerId, String note);
}