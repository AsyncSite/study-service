package com.asyncsite.studyservice.common.auth;

import com.asyncsite.studyservice.common.exception.ForbiddenException;
import com.asyncsite.studyservice.common.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.method.HandlerMethod;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticationInterceptor 테스트")
class AuthenticationInterceptorTest {

    @Mock
    private UserContextExtractor userContextExtractor;
    
    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private HandlerMethod handlerMethod;
    
    private AuthenticationInterceptor interceptor;
    
    @BeforeEach
    void setUp() {
        interceptor = new AuthenticationInterceptor(userContextExtractor);
    }

    @Test
    @DisplayName("HandlerMethod가 아닌 경우 인증을 건너뛴다")
    void shouldSkipAuthenticationForNonHandlerMethod() throws Exception {
        // given
        Object nonHandlerMethod = new Object();
        
        // when
        boolean result = interceptor.preHandle(request, response, nonHandlerMethod);
        
        // then
        assertThat(result).isTrue();
        verifyNoInteractions(userContextExtractor);
    }

    @Test
    @DisplayName("@RequireAuth 어노테이션이 없으면 인증을 건너뛴다")
    void shouldSkipAuthenticationWhenNoRequireAuthAnnotation() throws Exception {
        // given
        Method method = TestController.class.getMethod("publicMethod");
        when(handlerMethod.getMethodAnnotation(RequireAuth.class)).thenReturn(null);
        when(handlerMethod.getBeanType()).thenReturn((Class) TestController.class);
        
        // when
        boolean result = interceptor.preHandle(request, response, handlerMethod);
        
        // then
        assertThat(result).isTrue();
        verifyNoInteractions(userContextExtractor);
    }

    @Test
    @DisplayName("사용자 컨텍스트가 없으면 UnauthorizedException 발생")
    void shouldThrowUnauthorizedExceptionWhenNoUserContext() throws Exception {
        // given
        Method method = TestController.class.getMethod("requiresAuth");
        when(handlerMethod.getMethodAnnotation(RequireAuth.class))
                .thenReturn(method.getAnnotation(RequireAuth.class));
        when(handlerMethod.getBeanType()).thenReturn((Class) TestController.class);
        when(userContextExtractor.extractUserContext(request)).thenReturn(Optional.empty());
        
        // when & then
        assertThatThrownBy(() -> interceptor.preHandle(request, response, handlerMethod))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("인증이 필요합니다.");
    }

    @Test
    @DisplayName("필요한 권한이 없으면 ForbiddenException 발생")
    void shouldThrowForbiddenExceptionWhenInsufficientRoles() throws Exception {
        // given
        Method method = TestController.class.getMethod("requiresAdmin");
        when(handlerMethod.getMethodAnnotation(RequireAuth.class))
                .thenReturn(method.getAnnotation(RequireAuth.class));
        when(handlerMethod.getBeanType()).thenReturn((Class) TestController.class);
        
        UserContext userContext = new UserContext("user123", "user@example.com", "Test User", Set.of(Role.USER));
        when(userContextExtractor.extractUserContext(request)).thenReturn(Optional.of(userContext));
        
        // when & then
        assertThatThrownBy(() -> interceptor.preHandle(request, response, handlerMethod))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("필요한 권한이 없습니다.");
    }

    @Test
    @DisplayName("적절한 권한이 있으면 인증 성공")
    void shouldPassAuthenticationWithProperRole() throws Exception {
        // given
        Method method = TestController.class.getMethod("requiresAdmin");
        when(handlerMethod.getMethodAnnotation(RequireAuth.class))
                .thenReturn(method.getAnnotation(RequireAuth.class));
        when(handlerMethod.getBeanType()).thenReturn((Class) TestController.class);
        
        UserContext userContext = new UserContext("admin123", "admin@example.com", "Admin User", Set.of(Role.ADMIN));
        when(userContextExtractor.extractUserContext(request)).thenReturn(Optional.of(userContext));
        
        // when
        boolean result = interceptor.preHandle(request, response, handlerMethod);
        
        // then
        assertThat(result).isTrue();
        verify(request).setAttribute("userContext", userContext);
    }

    @Test
    @DisplayName("클래스 레벨 @RequireAuth 어노테이션도 처리한다")
    void shouldHandleClassLevelRequireAuthAnnotation() throws Exception {
        // given
        Method method = ClassLevelAuthController.class.getMethod("someMethod");
        when(handlerMethod.getMethodAnnotation(RequireAuth.class)).thenReturn(null);
        when(handlerMethod.getBeanType()).thenReturn((Class) ClassLevelAuthController.class);
        
        UserContext userContext = new UserContext("user123", "user@example.com", "Test User", Set.of(Role.USER));
        when(userContextExtractor.extractUserContext(request)).thenReturn(Optional.of(userContext));
        
        // when
        boolean result = interceptor.preHandle(request, response, handlerMethod);
        
        // then
        assertThat(result).isTrue();
        verify(request).setAttribute("userContext", userContext);
    }

    // 테스트용 컨트롤러 클래스들
    static class TestController {
        public void publicMethod() {}
        
        @RequireAuth
        public void requiresAuth() {}
        
        @RequireAuth(roles = {Role.ADMIN})
        public void requiresAdmin() {}
    }
    
    @RequireAuth(roles = {Role.USER})
    static class ClassLevelAuthController {
        public void someMethod() {}
    }
}