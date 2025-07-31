package com.asyncsite.studyservice.study.adapter.in.web;

import com.asyncsite.studyservice.common.auth.AuthenticationInterceptor;
import com.asyncsite.studyservice.common.auth.Role;
import com.asyncsite.studyservice.common.auth.UserContext;
import com.asyncsite.studyservice.common.config.WebConfig;
import com.asyncsite.studyservice.study.domain.model.Study;
import com.asyncsite.studyservice.study.domain.model.StudyStatus;
import com.asyncsite.studyservice.study.domain.port.in.GetStudyUseCase;
import com.asyncsite.studyservice.study.domain.port.in.ManageStudyUseCase;
import com.asyncsite.studyservice.study.domain.port.in.ProposeStudyUseCase;
import com.asyncsite.studyservice.study.adapter.in.web.mapper.StudyWebMapper;
import com.asyncsite.studyservice.common.auth.UserContextExtractor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = StudyController.class, excludeAutoConfiguration = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
    org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
})
@DisplayName("StudyController 인증 통합 테스트")
@Import({AuthenticationInterceptor.class, WebConfig.class}) // 커스텀 인터셉터 로드
@AutoConfigureWebMvc
class StudyControllerAuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockitoBean
    private ProposeStudyUseCase proposeStudyUseCase;
    
    @MockitoBean
    private ManageStudyUseCase manageStudyUseCase;
    
    @MockitoBean
    private GetStudyUseCase getStudyUseCase;
    
    @MockitoBean
    private StudyWebMapper studyWebMapper;
    
    @MockitoBean
    private UserContextExtractor userContextExtractor;

    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USER_EMAIL_HEADER = "X-User-Email";
    private static final String USER_NAME_HEADER = "X-User-Name";
    private static final String USER_ROLES_HEADER = "X-User-Roles";

    @Test
    @DisplayName("공개 엔드포인트는 인증 없이 접근 가능하다")
    void shouldAllowAccessToPublicEndpointsWithoutAuth() throws Exception {
        // given
        Study mockStudy = createMockStudy();
        Page<Study> mockPage = new PageImpl<>(List.of(mockStudy));
        
        when(getStudyUseCase.getAllStudies()).thenReturn(List.of(mockStudy));
        when(getStudyUseCase.getAllStudies(any(Pageable.class))).thenReturn(mockPage);
        when(getStudyUseCase.getStudyById(any(UUID.class))).thenReturn(Optional.of(mockStudy));
        
        // when & then - 전체 목록 조회
        mockMvc.perform(get("/api/v1/studies"))
                .andExpect(status().isOk());
        
        // when & then - 단일 스터디 조회
        mockMvc.perform(get("/api/v1/studies/" + UUID.randomUUID()))
                .andExpect(status().isOk());
        
        // when & then - 페이징 조회
        mockMvc.perform(get("/api/v1/studies/paged"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("보호된 엔드포인트는 인증 없이 접근 시 401 반환")
    void shouldReturn401ForProtectedEndpointsWithoutAuth() throws Exception {
        // given
        StudyCreateRequest request = new StudyCreateRequest("Test Study", "Description", "user123");
        
        // when & then
        mockMvc.perform(post("/api/v1/studies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("UNAUTHORIZED"));
    }

    @Test
    @DisplayName("USER 권한으로 스터디 제안이 가능하다")
    void shouldAllowStudyProposalWithUserRole() throws Exception {
        // given
        String userId = "user123";
        StudyCreateRequest request = new StudyCreateRequest("Test Study", "Description", userId);
        Study mockStudy = createMockStudy();
        UserContext userContext = new UserContext(userId, "user@example.com", "Test User", Set.of(Role.USER));
        
        when(userContextExtractor.extractUserContext(any())).thenReturn(Optional.of(userContext));
        when(proposeStudyUseCase.propose(any(), any(), any())).thenReturn(mockStudy);
        
        // when & then
        mockMvc.perform(post("/api/v1/studies")
                        .header(USER_ID_HEADER, userId)
                        .header(USER_EMAIL_HEADER, "user@example.com")
                        .header(USER_NAME_HEADER, "Test User")
                        .header(USER_ROLES_HEADER, "USER")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("다른 사용자의 proposerId로 스터디 제안 시 403 반환")
    void shouldReturn403WhenProposingStudyForAnotherUser() throws Exception {
        // given
        StudyCreateRequest request = new StudyCreateRequest("Test Study", "Description", "otherUser");
        
        // when & then
        mockMvc.perform(post("/api/v1/studies")
                        .header(USER_ID_HEADER, "user123")
                        .header(USER_EMAIL_HEADER, "user@example.com")
                        .header(USER_NAME_HEADER, "Test User")
                        .header(USER_ROLES_HEADER, "USER")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("FORBIDDEN"));
    }

    @Test
    @DisplayName("ADMIN 권한으로 스터디 승인이 가능하다")
    void shouldAllowStudyApprovalWithAdminRole() throws Exception {
        // given
        UUID studyId = UUID.randomUUID();
        Study mockStudy = createMockStudy();
        
        when(manageStudyUseCase.approve(studyId)).thenReturn(mockStudy);
        
        // when & then
        mockMvc.perform(patch("/api/v1/studies/" + studyId + "/approve")
                        .header(USER_ID_HEADER, "admin123")
                        .header(USER_EMAIL_HEADER, "admin@example.com")
                        .header(USER_NAME_HEADER, "Admin User")
                        .header(USER_ROLES_HEADER, "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("USER 권한으로 스터디 승인 시 403 반환")
    void shouldReturn403WhenUserTriesToApproveStudy() throws Exception {
        // given
        UUID studyId = UUID.randomUUID();
        
        // when & then
        mockMvc.perform(patch("/api/v1/studies/" + studyId + "/approve")
                        .header(USER_ID_HEADER, "user123")
                        .header(USER_EMAIL_HEADER, "user@example.com")
                        .header(USER_NAME_HEADER, "Test User")
                        .header(USER_ROLES_HEADER, "USER"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("FORBIDDEN"));
    }

    @Test
    @DisplayName("복수 권한 중 하나라도 만족하면 접근 가능하다")
    void shouldAllowAccessWithAnyOfRequiredRoles() throws Exception {
        // given
        String userId = "user123";
        StudyCreateRequest request = new StudyCreateRequest("Test Study", "Description", userId);
        Study mockStudy = createMockStudy();
        
        when(proposeStudyUseCase.propose(any(), any(), any())).thenReturn(mockStudy);
        
        // when & then - ADMIN 권한으로도 스터디 제안 가능 (USER, ADMIN 중 하나)
        mockMvc.perform(post("/api/v1/studies")
                        .header(USER_ID_HEADER, userId)
                        .header(USER_EMAIL_HEADER, "admin@example.com")
                        .header(USER_NAME_HEADER, "Admin User")
                        .header(USER_ROLES_HEADER, "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("잘못된 역할 헤더가 있어도 알려진 역할만 파싱된다")
    void shouldParseOnlyKnownRolesFromHeader() throws Exception {
        // given
        String userId = "user123";  
        StudyCreateRequest request = new StudyCreateRequest("Test Study", "Description", userId);
        Study mockStudy = createMockStudy();
        UserContext userContext = new UserContext(userId, "user@example.com", "Test User", Set.of(Role.USER));
        
        when(userContextExtractor.extractUserContext(any())).thenReturn(Optional.of(userContext));
        when(proposeStudyUseCase.propose(any(), any(), any())).thenReturn(mockStudy);
        
        // when & then - 알 수 없는 역할(UNKNOWN_ROLE)이 포함되어 있지만 USER는 유효하므로 성공
        mockMvc.perform(post("/api/v1/studies")
                        .header(USER_ID_HEADER, userId)
                        .header(USER_EMAIL_HEADER, "user@example.com")
                        .header(USER_NAME_HEADER, "Test User")
                        .header(USER_ROLES_HEADER, "USER,UNKNOWN_ROLE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));
    }

    private Study createMockStudy() {
        return new Study(
                UUID.randomUUID(),
                "Test Study",
                "Test Description", 
                "user123",
                StudyStatus.PENDING,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null, null, null, null, null, null, null, 0,
                null, null, null
        );
    }
}
