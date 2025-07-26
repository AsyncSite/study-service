package com.asyncsite.studyservice.membership.domain.port.in;

import java.util.UUID;

public interface RejectApplicationUseCase {
    void reject(UUID applicationId, String reviewerId, String reason);
}