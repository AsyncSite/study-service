package com.asyncsite.studyservice.adapter.in.web;

import com.asyncsite.studyservice.adapter.in.web.mapper.StudyWebMapper;
import com.asyncsite.studyservice.common.ApiResponse;
import com.asyncsite.studyservice.domain.model.Study;
import com.asyncsite.studyservice.domain.port.in.GetStudyUseCase;
import com.asyncsite.studyservice.domain.port.in.ManageStudyUseCase;
import com.asyncsite.studyservice.domain.port.in.ProposeStudyUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Tag(name = "Study API", description = "스터디 관리 API")
@RestController
@RequestMapping("/api/studies")
@RequiredArgsConstructor
public class StudyController {
    private final ProposeStudyUseCase proposeStudyUseCase;
    private final ManageStudyUseCase manageStudyUseCase;
    private final GetStudyUseCase getStudyUseCase;
    private final StudyWebMapper studyWebMapper;

    @Operation(summary = "스터디 제안", description = "새로운 스터디를 제안합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "스터디 제안 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<StudyResponse>> propose(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "스터디 제안 정보") 
            @RequestBody StudyCreateRequest request) {
        Study study = proposeStudyUseCase.propose(request.title(), request.description(), request.proposerId());
        StudyResponse response = studyWebMapper.toResponse(study);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "스터디 제안이 성공적으로 등록되었습니다."));
    }

    @Operation(summary = "스터디 승인", description = "제안된 스터디를 승인합니다. (운영자 권한 필요)")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스터디 승인 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스터디를 찾을 수 없음"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음")
    })
    @PatchMapping("/{studyId}/approve")
    public ResponseEntity<ApiResponse<StudyResponse>> approve(
            @Parameter(description = "스터디 ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID studyId) {
        Study study = manageStudyUseCase.approve(studyId);
        StudyResponse response = studyWebMapper.toResponse(study);
        return ResponseEntity.ok(ApiResponse.success(response, "스터디가 성공적으로 승인되었습니다."));
    }

    @Operation(summary = "스터디 거절", description = "제안된 스터디를 거절합니다. (운영자 권한 필요)")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스터디 거절 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스터디를 찾을 수 없음"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음")
    })
    @PatchMapping("/{studyId}/reject")
    public ResponseEntity<ApiResponse<StudyResponse>> reject(
            @Parameter(description = "스터디 ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID studyId) {
        Study study = manageStudyUseCase.reject(studyId);
        StudyResponse response = studyWebMapper.toResponse(study);
        return ResponseEntity.ok(ApiResponse.success(response, "스터디가 거절되었습니다."));
    }

    @Operation(summary = "스터디 종료", description = "진행 중인 스터디를 종료합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스터디 종료 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스터디를 찾을 수 없음")
    })
    @DeleteMapping("/{studyId}")
    public ResponseEntity<ApiResponse<StudyResponse>> terminate(
            @Parameter(description = "스터디 ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID studyId) {
        Study study = manageStudyUseCase.terminate(studyId);
        StudyResponse response = studyWebMapper.toResponse(study);
        return ResponseEntity.ok(ApiResponse.success(response, "스터디가 종료되었습니다."));
    }

    @Operation(summary = "전체 스터디 목록 조회", description = "승인된 모든 스터디 목록을 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudyResponse>>> getAllStudies() {
        List<Study> studies = getStudyUseCase.getAllStudies();
        List<StudyResponse> responses = studies.stream()
                .map(studyWebMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
    
    @Operation(summary = "스터디 목록 페이징 조회", description = "승인된 스터디 목록을 페이징하여 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/paged")
    public ResponseEntity<ApiResponse<Page<StudyResponse>>> getAllStudies(
            @Parameter(description = "페이징 정보 (page, size, sort)") Pageable pageable) {
        Page<Study> studies = getStudyUseCase.getAllStudies(pageable);
        Page<StudyResponse> responses = studies.map(studyWebMapper::toResponse);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @Operation(summary = "스터디 상세 조회", description = "특정 스터디의 상세 정보를 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "스터디를 찾을 수 없음")
    })
    @GetMapping("/{studyId}")
    public ResponseEntity<ApiResponse<StudyResponse>> getStudyById(
            @Parameter(description = "스터디 ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID studyId) {
        Optional<Study> study = getStudyUseCase.getStudyById(studyId);
        return study.map(s -> {
            StudyResponse response = studyWebMapper.toResponse(s);
            return ResponseEntity.ok(ApiResponse.success(response));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.failure("스터디를 찾을 수 없습니다.")));
    }
}