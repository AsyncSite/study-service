package com.asyncsite.studyservice.study;

import com.asyncsite.studyservice.study.adapter.in.web.StudyCreateRequest;
import com.asyncsite.studyservice.study.adapter.in.web.StudyResponse;
import com.asyncsite.studyservice.study.domain.model.StudyStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DisplayName("Study 통합 테스트")
class StudyIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @Transactional
    @DisplayName("스터디 생성 API 통합 테스트")
    void givenValidStudyRequest_whenCreateStudy_thenReturnsCreatedStudy() throws Exception {
        // given
        final StudyCreateRequest request = new StudyCreateRequest(
                "Spring Boot 스터디",
                "Spring Boot를 깊이 있게 학습하는 스터디",
                "user001"
        );

        // when & then
        mockMvc.perform(post("/api/v1/studies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.title").value("Spring Boot 스터디"))
                .andExpect(jsonPath("$.data.description").value("Spring Boot를 깊이 있게 학습하는 스터디"))
                .andExpect(jsonPath("$.data.proposerId").value("user001"))
                .andExpect(jsonPath("$.data.status").value("PENDING"))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.createdAt").exists())
                .andExpect(jsonPath("$.data.updatedAt").exists());
    }

    @Test
    @WithMockUser
    @Transactional
    @DisplayName("스터디 목록 조회 API 통합 테스트")
    void whenGetAllStudies_thenReturnsStudyList() throws Exception {
        // given - 스터디 생성
        final StudyCreateRequest request1 = new StudyCreateRequest("Study 1", "Description 1", "user001");
        final StudyCreateRequest request2 = new StudyCreateRequest("Study 2", "Description 2", "user002");

        mockMvc.perform(post("/api/v1/studies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)));

        mockMvc.perform(post("/api/v1/studies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)));

        // when & then
        mockMvc.perform(get("/api/v1/studies/paged"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(2))
                .andExpect(jsonPath("$.data.content[0].title").exists())
                .andExpect(jsonPath("$.data.content[1].title").exists())
                .andExpect(jsonPath("$.data.totalElements").value(2))
                .andExpect(jsonPath("$.data.totalPages").value(1));
    }

    @Test
    @WithMockUser
    @Transactional
    @DisplayName("스터디 승인 API 통합 테스트")
    void givenPendingStudy_whenApproveStudy_thenReturnsApprovedStudy() throws Exception {
        // given - 스터디 생성
        final StudyCreateRequest createRequest = new StudyCreateRequest(
                "Approval Test Study",
                "승인 테스트용 스터디",
                "user001"
        );

        final String createResponse = mockMvc.perform(post("/api/v1/studies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        final StudyResponse createdStudy = ApiResponseWrapper.extractData(createResponse, StudyResponse.class, objectMapper);

        // when & then
        mockMvc.perform(patch("/api/v1/studies/{studyId}/approve", createdStudy.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(createdStudy.id().toString()))
                .andExpect(jsonPath("$.data.status").value("APPROVED"))
                .andExpect(jsonPath("$.data.title").value("Approval Test Study"));
    }

    @Test
    @WithMockUser
    @Transactional
    @DisplayName("스터디 거절 API 통합 테스트")
    void givenPendingStudy_whenRejectStudy_thenReturnsRejectedStudy() throws Exception {
        // given - 스터디 생성
        final StudyCreateRequest createRequest = new StudyCreateRequest(
                "Rejection Test Study",
                "거절 테스트용 스터디",
                "user001"
        );

        final String createResponse = mockMvc.perform(post("/api/v1/studies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        final StudyResponse createdStudy = ApiResponseWrapper.extractData(createResponse, StudyResponse.class, objectMapper);

        // when & then
        mockMvc.perform(patch("/api/v1/studies/{studyId}/reject", createdStudy.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(createdStudy.id().toString()))
                .andExpect(jsonPath("$.data.status").value("REJECTED"))
                .andExpect(jsonPath("$.data.title").value("Rejection Test Study"));
    }

    @Test
    @WithMockUser
    @Transactional
    @DisplayName("스터디 단건 조회 API 통합 테스트")
    void givenExistingStudy_whenGetStudyById_thenReturnsStudy() throws Exception {
        // given - 스터디 생성
        final StudyCreateRequest createRequest = new StudyCreateRequest(
                "Get Test Study",
                "조회 테스트용 스터디",
                "user001"
        );

        final String createResponse = mockMvc.perform(post("/api/v1/studies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        final StudyResponse createdStudy = ApiResponseWrapper.extractData(createResponse, StudyResponse.class, objectMapper);

        // when & then
        mockMvc.perform(get("/api/v1/studies/{studyId}", createdStudy.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(createdStudy.id().toString()))
                .andExpect(jsonPath("$.data.title").value("Get Test Study"))
                .andExpect(jsonPath("$.data.description").value("조회 테스트용 스터디"))
                .andExpect(jsonPath("$.data.proposerId").value("user001"))
                .andExpect(jsonPath("$.data.status").value("PENDING"));
    }

    @Test
    @WithMockUser
    @Transactional
    @DisplayName("존재하지 않는 스터디 조회 시 404 반환")
    void givenNonExistentStudyId_whenGetStudyById_thenReturns404() throws Exception {
        // given
        final String nonExistentId = "123e4567-e89b-12d3-a456-426614174000";

        // when & then
        mockMvc.perform(get("/api/v1/studies/{studyId}", nonExistentId))
                .andExpect(status().isNotFound());
    }
}