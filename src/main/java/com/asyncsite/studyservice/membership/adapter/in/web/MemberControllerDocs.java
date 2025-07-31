package com.asyncsite.studyservice.membership.adapter.in.web;

import com.asyncsite.coreplatform.common.dto.ApiResponse;
import com.asyncsite.studyservice.membership.adapter.in.web.dto.ChangeMemberRoleRequest;
import com.asyncsite.studyservice.membership.adapter.in.web.dto.MemberResponse;
import com.asyncsite.studyservice.membership.adapter.in.web.dto.WarnMemberRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.UUID;

@Tag(name = "Member Management", description = "스터디 멤버 관리 API")
public interface MemberControllerDocs {

    @Operation(summary = "멤버 목록 조회", description = "스터디 멤버 목록을 조회합니다")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ApiResponse<Page<MemberResponse>> getMembers(
            @Parameter(description = "스터디 ID") @PathVariable UUID studyId,
            @Parameter(hidden = true) Pageable pageable);

    @Operation(summary = "멤버 상세 조회", description = "멤버 상세 정보를 조회합니다")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "멤버를 찾을 수 없음")
    })
    ApiResponse<MemberResponse> getMember(
            @Parameter(description = "스터디 ID") @PathVariable UUID studyId,
            @Parameter(description = "멤버 ID") @PathVariable UUID memberId);

    @Operation(summary = "멤버 수 조회", description = "스터디의 총 멤버 수를 조회합니다")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ApiResponse<Integer> getMemberCount(@Parameter(description = "스터디 ID") @PathVariable UUID studyId);

    @Operation(summary = "멤버 역할 변경", description = "멤버의 역할을 변경합니다 (리더 전용)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "역할 변경 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "멤버를 찾을 수 없음")
    })
    ApiResponse<Void> changeMemberRole(
            @Parameter(description = "스터디 ID") @PathVariable UUID studyId,
            @Parameter(description = "멤버 ID") @PathVariable UUID memberId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "역할 변경 정보") @Valid @RequestBody ChangeMemberRoleRequest request);

    @Operation(summary = "멤버 제명", description = "멤버를 스터디에서 제명합니다")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "멤버 제명 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "멤버를 찾을 수 없음")
    })
    ApiResponse<Void> removeMember(
            @Parameter(description = "스터디 ID") @PathVariable UUID studyId,
            @Parameter(description = "멤버 ID") @PathVariable UUID memberId,
            @Parameter(description = "요청자 ID") @RequestParam String requesterId);

    @Operation(summary = "멤버 경고", description = "멤버에게 경고를 발송합니다")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "경고 발송 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "멤버를 찾을 수 없음")
    })
    ApiResponse<Void> warnMember(
            @Parameter(description = "스터디 ID") @PathVariable UUID studyId,
            @Parameter(description = "멤버 ID") @PathVariable UUID memberId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "경고 정보") @Valid @RequestBody WarnMemberRequest request);

    @Operation(summary = "스터디 탈퇴", description = "스터디에서 자발적으로 탈퇴합니다")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "탈퇴 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "멤버를 찾을 수 없음")
    })
    ApiResponse<Void> leaveStudy(
            @Parameter(description = "스터디 ID") @PathVariable UUID studyId,
            @Parameter(description = "사용자 ID") @RequestParam String userId);

    @Operation(summary = "멤버 통계", description = "스터디 멤버 통계를 조회합니다")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ApiResponse<Map<String, Object>> getMemberStatistics(@Parameter(description = "스터디 ID") @PathVariable UUID studyId);
}