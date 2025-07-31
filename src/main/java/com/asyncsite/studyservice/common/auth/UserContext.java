package com.asyncsite.studyservice.common.auth;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * JWT 토큰에서 추출된 사용자 컨텍스트 정보
 * Gateway에서 X-User-* 헤더로 전달되는 사용자 정보를 담는 모델
 */
public record UserContext(
    String userId,
    String email,
    String name,
    Set<Role> roles
) {
    
    public boolean hasRole(Role role) {
        return roles != null && roles.contains(role);
    }
    
    public boolean hasRole(String roleString) {
        try {
            Role role = Role.fromValue(roleString);
            return hasRole(role);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    public boolean isAdmin() {
        return hasRole(Role.ADMIN);
    }
    
    public boolean isUser() {
        return hasRole(Role.USER);
    }
    
    /**
     * 문자열 목록을 Role Set으로 변환하는 헬퍼 메서드
     */
    public static Set<Role> parseRoles(List<String> roleStrings) {
        if (roleStrings == null) {
            return Set.of();
        }
        
        return roleStrings.stream()
                .map(roleString -> {
                    try {
                        return Role.fromValue(roleString);
                    } catch (IllegalArgumentException e) {
                        // 알 수 없는 역할은 무시
                        return null;
                    }
                })
                .filter(role -> role != null)
                .collect(Collectors.toSet());
    }
}