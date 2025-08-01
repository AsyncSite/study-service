package com.asyncsite.studyservice.common.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * HTTP 헤더에서 사용자 컨텍스트 정보를 추출하는 유틸리티
 */
@Component
public class UserContextExtractor {
    
    private static final String HEADER_USER_ID = "X-User-Id";
    private static final String HEADER_USER_EMAIL = "X-User-Email";
    private static final String HEADER_USER_NAME = "X-User-Name";
    private static final String HEADER_USER_ROLES = "X-User-Roles";
    
    public Optional<UserContext> extractUserContext(HttpServletRequest request) {
        final String userId = request.getHeader(HEADER_USER_ID);
        final String email = request.getHeader(HEADER_USER_EMAIL);
        final String name = request.getHeader(HEADER_USER_NAME);
        final String rolesHeader = request.getHeader(HEADER_USER_ROLES);
        
        if (userId == null || userId.trim().isEmpty()) {
            return Optional.empty();
        }
        
        final List<String> roleStrings = parseRoleStrings(rolesHeader);
        final Set<Role> roles = UserContext.parseRoles(roleStrings);
        
        return Optional.of(new UserContext(userId, email, name, roles));
    }
    
    private List<String> parseRoleStrings(String rolesHeader) {
        if (rolesHeader == null || rolesHeader.trim().isEmpty()) {
            return List.of();
        }
        
        return Arrays.stream(rolesHeader.split(","))
                .map(String::trim)
                .filter(role -> !role.isEmpty())
                .toList();
    }
}