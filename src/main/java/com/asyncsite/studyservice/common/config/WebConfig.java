package com.asyncsite.studyservice.common.config;

import com.asyncsite.studyservice.common.auth.AuthenticationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 설정
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/api/**")  // API 경로에만 적용
                .excludePathPatterns(
                        "/api/v1/studies",           // 전체 스터디 목록 조회 (공개)
                        "/api/v1/studies/paged",     // 페이지네이션 목록 조회 (공개)
                        "/api/v1/studies/*",         // 단일 스터디 조회 (공개) - UUID 패턴
                        "/api/v1/studies/by-status/**", // 상태별 조회 (공개)
                        "/api/v1/studies/recruiting", // 모집중인 스터디 (공개)
                        "/api/health/**",            // 헬스 체크
                        "/api/actuator/**"           // 액츄에이터
                );
    }
}