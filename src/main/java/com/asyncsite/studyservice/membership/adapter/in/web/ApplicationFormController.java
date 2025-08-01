package com.asyncsite.studyservice.membership.adapter.in.web;

import com.asyncsite.coreplatform.common.dto.ApiResponse;
import com.asyncsite.studyservice.membership.adapter.in.web.dto.ApplicationFormRequest;
import com.asyncsite.studyservice.membership.adapter.in.web.dto.ApplicationFormResponse;
import com.asyncsite.studyservice.membership.domain.model.ApplicationForm;
import com.asyncsite.studyservice.membership.domain.port.in.ApplicationFormUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/v1/studies/{studyId}/application-form")
@Tag(name = "Application Form Management", description = "지원서 양식 관리 API")
@RequiredArgsConstructor
@Slf4j
public class ApplicationFormController implements ApplicationFormControllerDocs {
    
    private final ApplicationFormUseCase applicationFormUseCase;
    
    @Override
    @GetMapping
    public ApiResponse<ApplicationFormResponse> getApplicationForm(
            @PathVariable UUID studyId) {
        
        log.info("지원서 양식 조회 - studyId: {}", studyId);
        
        Optional<ApplicationForm> form = applicationFormUseCase.getFormByStudyId(studyId);
        if (form.isEmpty()) {
            return ApiResponse.<ApplicationFormResponse>error("FORM_NOT_FOUND", "활성화된 지원서 양식이 없습니다.", null);
        }
        
        return ApiResponse.success(ApplicationFormResponse.from(form.get()));
    }
    
    @Override
    @PostMapping
    public ApiResponse<ApplicationFormResponse> createApplicationForm(
            @PathVariable UUID studyId,
            @Valid @RequestBody ApplicationFormRequest request) {
        
        log.info("지원서 양식 생성 - studyId: {}, questions: {}", studyId, request.getQuestions().size());
        
        ApplicationForm form = applicationFormUseCase.createForm(studyId, request.toQuestions());
        return ApiResponse.success(ApplicationFormResponse.from(form));
    }
    
    @Override
    @PutMapping("/{formId}")
    public ApiResponse<ApplicationFormResponse> updateApplicationForm(
            @PathVariable UUID studyId,
            @PathVariable UUID formId,
            @Valid @RequestBody ApplicationFormRequest request) {
        
        log.info("지원서 양식 수정 - formId: {}", formId);
        
        ApplicationForm form = applicationFormUseCase.updateForm(formId, request.toQuestions());
        return ApiResponse.success(ApplicationFormResponse.from(form));
    }
    
    @Override
    @DeleteMapping("/{formId}")
    public ApiResponse<Void> deactivateApplicationForm(
            @PathVariable UUID studyId,
            @PathVariable UUID formId) {
        
        log.info("지원서 양식 비활성화 - formId: {}", formId);
        
        applicationFormUseCase.deactivateForm(formId);
        return ApiResponse.success(null);
    }
    
    @Override
    @GetMapping("/template")
    public ApiResponse<Map<String, Object>> getApplicationFormTemplate() {
        
        log.info("지원서 양식 템플릿 조회");
        
        Map<String, Object> template = Map.of(
                "basicQuestions", List.of(
                        Map.of("content", "지원 동기", "type", "TEXTAREA", "isRequired", true),
                        Map.of("content", "관련 경험", "type", "TEXTAREA", "isRequired", true),
                        Map.of("content", "포트폴리오 URL", "type", "TEXT", "isRequired", false)
                ),
                "suggestedQuestions", List.of(
                        Map.of("content", "희망하는 학습 목표", "type", "TEXT"),
                        Map.of("content", "참여 가능 시간", "type", "MULTIPLE_CHOICE", 
                               "options", List.of("평일 저녁", "평일 오전", "주말 오전", "주말 오후")),
                        Map.of("content", "선호하는 스터디 방식", "type", "CHECKBOX",
                               "options", List.of("발표", "코드리뷰", "프로젝트", "문제풀이"))
                )
        );
        
        return ApiResponse.success(template);
    }
}