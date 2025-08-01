package com.asyncsite.studyservice.common.auth;

import com.asyncsite.studyservice.common.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 현재 요청의 사용자 컨텍스트를 가져오는 유틸리티 클래스
 */
public final class UserContextHolder {
    
    private static final String USER_CONTEXT_ATTRIBUTE = "userContext";
    
    private UserContextHolder() {
        // 유틸리티 클래스
    }
    
    /**
     * 현재 요청의 사용자 컨텍스트를 가져옵니다.
     * 
     * @param request HTTP 요청 객체
     * @return 사용자 컨텍스트
     * @throws UnauthorizedException 사용자 컨텍스트가 없는 경우
     */
    public static UserContext getCurrentUserContext(HttpServletRequest request) {
        Object userContext = request.getAttribute(USER_CONTEXT_ATTRIBUTE);
        if (userContext instanceof UserContext) {
            return (UserContext) userContext;
        }
        throw new UnauthorizedException("사용자 컨텍스트를 찾을 수 없습니다.");
    }
    
    /**
     * 현재 요청의 사용자 ID를 가져옵니다.
     */
    public static String getCurrentUserId(HttpServletRequest request) {
        return getCurrentUserContext(request).userId();
    }
    
    /**
     * 현재 사용자가 특정 권한을 가지고 있는지 확인합니다.
     */
    public static boolean hasRole(HttpServletRequest request, Role role) {
        return getCurrentUserContext(request).hasRole(role);
    }
    
    /**
     * 현재 사용자가 ADMIN 권한을 가지고 있는지 확인합니다.
     */
    public static boolean isAdmin(HttpServletRequest request) {
        return getCurrentUserContext(request).isAdmin();
    }
}