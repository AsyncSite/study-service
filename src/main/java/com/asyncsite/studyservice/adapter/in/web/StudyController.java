package com.asyncsite.studyservice.adapter.in.web;

import com.asyncsite.studyservice.adapter.in.web.mapper.StudyWebMapper;
import com.asyncsite.coreplatform.common.dto.ApiResponse;
import com.asyncsite.studyservice.domain.model.Study;
import com.asyncsite.studyservice.domain.service.StudyNotFoundException;
import com.asyncsite.studyservice.domain.port.in.GetStudyUseCase;
import com.asyncsite.studyservice.domain.port.in.ManageStudyUseCase;
import com.asyncsite.studyservice.domain.port.in.ProposeStudyUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<StudyResponse>> propose(@Valid @RequestBody StudyCreateRequest request) {
        Study study = proposeStudyUseCase.propose(request.title(), request.description(), request.proposerId());
        StudyResponse response = studyWebMapper.toResponse(study);
        return ApiResponse.created(response);
    }

    @Override
    @PatchMapping("/{studyId}/approve")
    public ResponseEntity<ApiResponse<StudyResponse>> approve(@PathVariable UUID studyId) {
        Study study = manageStudyUseCase.approve(studyId);
        StudyResponse response = studyWebMapper.toResponse(study);
        return ApiResponse.ok(response);
    }

    @Override
    @PatchMapping("/{studyId}/reject")
    public ResponseEntity<ApiResponse<StudyResponse>> reject(@PathVariable UUID studyId) {
        Study study = manageStudyUseCase.reject(studyId);
        StudyResponse response = studyWebMapper.toResponse(study);
        return ApiResponse.ok(response);
    }

    @Override
    @DeleteMapping("/{studyId}")
    public ResponseEntity<ApiResponse<StudyResponse>> terminate(@PathVariable UUID studyId) {
        Study study = manageStudyUseCase.terminate(studyId);
        StudyResponse response = studyWebMapper.toResponse(study);
        return ApiResponse.ok(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudyResponse>>> getAllStudies() {
        List<Study> studies = getStudyUseCase.getAllStudies();
        List<StudyResponse> responses = studies.stream()
                .map(studyWebMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @Override
    @GetMapping("/paged")
    public ResponseEntity<ApiResponse<Page<StudyResponse>>> getAllStudies(Pageable pageable) {
        Page<Study> studies = getStudyUseCase.getAllStudies(pageable);
        Page<StudyResponse> responses = studies.map(studyWebMapper::toResponse);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @Override
    @GetMapping("/{studyId}")
    public ResponseEntity<ApiResponse<StudyResponse>> getStudyById(@PathVariable UUID studyId) {
        Optional<Study> study = getStudyUseCase.getStudyById(studyId);
        if (study.isEmpty()) {
            throw new StudyNotFoundException("Study not found with id: " + studyId);
        }
        
        StudyResponse response = studyWebMapper.toResponse(study.get());
        return ApiResponse.ok(response);
    }
}
