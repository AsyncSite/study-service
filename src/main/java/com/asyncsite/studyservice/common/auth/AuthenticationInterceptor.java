package com.asyncsite.studyservice.common.auth;

import com.asyncsite.studyservice.common.exception.ForbiddenException;
import com.asyncsite.studyservice.common.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.Optional;

/**
 * JWT 기반 인증 및 권한 검사 인터셉터
 * @RequireAuth 어노테이션이 있는 엔드포인트에 대해 자동으로 권한 검사 수행
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final UserContextExtractor userContextExtractor;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // HandlerMethod가 아닌 경우 (예: 정적 리소스) 통과
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        // @RequireAuth 어노테이션 확인 (메서드 레벨 우선, 클래스 레벨 fallback)
        RequireAuth methodAuth = handlerMethod.getMethodAnnotation(RequireAuth.class);
        RequireAuth classAuth = handlerMethod.getBeanType().getAnnotation(RequireAuth.class);
        RequireAuth requireAuth = methodAuth != null ? methodAuth : classAuth;

        // @RequireAuth 어노테이션이 없으면 인증 불필요
        if (requireAuth == null) {
            return true;
        }

        // 사용자 컨텍스트 추출
        Optional<UserContext> userContextOpt = userContextExtractor.extractUserContext(request);
        if (userContextOpt.isEmpty()) {
            log.warn("Authentication required but no user context found for request: {} {}", 
                    request.getMethod(), request.getRequestURI());
            throw new UnauthorizedException("인증이 필요합니다.");
        }

        UserContext userContext = userContextOpt.get();
        
        // 권한 체크
        Role[] requiredRoles = requireAuth.roles();
        if (requiredRoles.length > 0) {
            boolean hasRequiredRole = Arrays.stream(requiredRoles)
                    .anyMatch(userContext::hasRole);
            
            if (!hasRequiredRole) {
                log.warn("Access denied for user {} to {} {}. Required roles: {}, User roles: {}", 
                        userContext.userId(), request.getMethod(), request.getRequestURI(), 
                        Arrays.toString(requiredRoles), userContext.roles());
                throw new ForbiddenException("필요한 권한이 없습니다.");
            }
        }

        // 요청 속성에 사용자 컨텍스트 저장 (컨트롤러에서 사용 가능)
        request.setAttribute("userContext", userContext);
        
        log.debug("Authentication successful for user {} accessing {} {}", 
                userContext.userId(), request.getMethod(), request.getRequestURI());
        
        return true;
    }
}