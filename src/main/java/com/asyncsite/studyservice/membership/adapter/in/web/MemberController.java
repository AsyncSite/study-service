package com.asyncsite.studyservice.membership.adapter.in.web;

import com.asyncsite.coreplatform.common.dto.ApiResponse;
import com.asyncsite.studyservice.membership.adapter.in.web.dto.ChangeMemberRoleRequest;
import com.asyncsite.studyservice.membership.adapter.in.web.dto.MemberResponse;
import com.asyncsite.studyservice.membership.adapter.in.web.dto.WarnMemberRequest;
import com.asyncsite.studyservice.membership.domain.model.Member;
import com.asyncsite.studyservice.membership.domain.port.in.MemberUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/studies/{studyId}/members")
@Tag(name = "Member Management", description = "스터디 멤버 관리 API")
@RequiredArgsConstructor
public class MemberController implements MemberControllerDocs {
    
    private final MemberUseCase memberUseCase;

    @Override
    @GetMapping
    public ApiResponse<Page<MemberResponse>> getMembers(
            @PathVariable UUID studyId,
            Pageable pageable) {
        Page<Member> members = memberUseCase.getMembers(studyId, pageable);
        Page<MemberResponse> responsePage = members.map(MemberResponse::from);
        return ApiResponse.success(responsePage);
    }

    @Override
    @GetMapping("/{memberId}")
    public ApiResponse<MemberResponse> getMember(
            @PathVariable UUID studyId,
            @PathVariable UUID memberId) {
        Member member = memberUseCase.getMember(studyId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));
        return ApiResponse.success(MemberResponse.from(member));
    }

    @Override
    @GetMapping("/count")
    public ApiResponse<Integer> getMemberCount(@PathVariable UUID studyId) {
        return ApiResponse.success(memberUseCase.getMemberCount(studyId));
    }

    @Override
    @PutMapping("/{memberId}/role")
    public ApiResponse<Void> changeMemberRole(
            @PathVariable UUID studyId,
            @PathVariable UUID memberId,
            @RequestBody ChangeMemberRoleRequest request) {
        memberUseCase.changeMemberRole(studyId, memberId, request.getRequesterId(), request.getNewRole());
        return ApiResponse.success(null);
    }

    @Override
    @DeleteMapping("/{memberId}")
    public ApiResponse<Void> removeMember(
            @PathVariable UUID studyId,
            @PathVariable UUID memberId,
            @RequestParam String requesterId) {
        memberUseCase.removeMember(studyId, memberId, requesterId);
        return ApiResponse.success(null);
    }

    @Override
    @PostMapping("/{memberId}/warnings")
    public ApiResponse<Void> warnMember(
            @PathVariable UUID studyId,
            @PathVariable UUID memberId,
            @RequestBody WarnMemberRequest request) {
        memberUseCase.warnMember(studyId, memberId, request.getRequesterId(), request.getReason());
        return ApiResponse.success(null);
    }

    @Override
    @PostMapping("/leave")
    public ApiResponse<Void> leaveStudy(
            @PathVariable UUID studyId,
            @RequestParam String userId) {
        memberUseCase.leaveStudy(studyId, userId);
        return ApiResponse.success(null);
    }

    @Override
    @GetMapping("/statistics")
    public ApiResponse<Map<String, Object>> getMemberStatistics(@PathVariable UUID studyId) {
        return ApiResponse.success(memberUseCase.getMemberStatistics(studyId));
    }
}