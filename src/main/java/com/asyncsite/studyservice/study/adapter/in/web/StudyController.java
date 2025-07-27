package com.asyncsite.studyservice.study.adapter.in.web;

import com.asyncsite.studyservice.study.adapter.in.web.mapper.StudyWebMapper;
import com.asyncsite.coreplatform.common.dto.ApiResponse;
import com.asyncsite.studyservice.study.domain.model.Study;
import com.asyncsite.studyservice.study.domain.service.StudyNotFoundException;
import com.asyncsite.studyservice.study.domain.port.in.GetStudyUseCase;
import com.asyncsite.studyservice.study.domain.port.in.ManageStudyUseCase;
import com.asyncsite.studyservice.study.domain.port.in.ProposeStudyUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/studies")
@RequiredArgsConstructor
public class StudyController implements StudyControllerDocs {
    private final ProposeStudyUseCase proposeStudyUseCase;
    private final ManageStudyUseCase manageStudyUseCase;
    private final GetStudyUseCase getStudyUseCase;
    private final StudyWebMapper studyWebMapper;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<StudyResponse> propose(@Valid @RequestBody StudyCreateRequest request) {
        Study study = proposeStudyUseCase.propose(request.title(), request.description(), request.proposerId());
        StudyResponse response = studyWebMapper.toResponse(study);
        return ApiResponse.success(response);
    }

    @Override
    @PatchMapping("/{studyId}/approve")
    public ApiResponse<StudyResponse> approve(@PathVariable UUID studyId) {
        Study study = manageStudyUseCase.approve(studyId);
        StudyResponse response = studyWebMapper.toResponse(study);
        return ApiResponse.success(response);
    }

    @Override
    @PatchMapping("/{studyId}/reject")
    public ApiResponse<StudyResponse> reject(@PathVariable UUID studyId) {
        Study study = manageStudyUseCase.reject(studyId);
        StudyResponse response = studyWebMapper.toResponse(study);
        return ApiResponse.success(response);
    }

    @Override
    @DeleteMapping("/{studyId}")
    public ApiResponse<StudyResponse> terminate(@PathVariable UUID studyId) {
        Study study = manageStudyUseCase.terminate(studyId);
        StudyResponse response = studyWebMapper.toResponse(study);
        return ApiResponse.success(response);
    }

    @Override
    @GetMapping
    public ApiResponse<List<StudyResponse>> getAllStudies() {
        List<Study> studies = getStudyUseCase.getAllStudies();
        List<StudyResponse> responses = studies.stream()
                .map(studyWebMapper::toResponse)
                .toList();
        return ApiResponse.success(responses);
    }

    @Override
    @GetMapping("/paged")
    public ApiResponse<Page<StudyResponse>> getAllStudies(Pageable pageable) {
        Page<Study> studies = getStudyUseCase.getAllStudies(pageable);
        Page<StudyResponse> responses = studies.map(studyWebMapper::toResponse);
        return ApiResponse.success(responses);
    }

    @Override
    @GetMapping("/{studyId}")
    public ApiResponse<StudyResponse> getStudyById(@PathVariable UUID studyId) {
        Optional<Study> study = getStudyUseCase.getStudyById(studyId);
        if (study.isEmpty()) {
            throw new StudyNotFoundException("Study not found with id: " + studyId);
        }
        
        StudyResponse response = studyWebMapper.toResponse(study.get());
        return ApiResponse.success(response);
    }
}
