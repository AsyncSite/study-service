package com.asyncsite.studyservice.common.domain.security;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GatewayAuthenticationDetails {
    private final String userId;
    private final String userName;
    private final String userEmail;
    private final String roles;
}