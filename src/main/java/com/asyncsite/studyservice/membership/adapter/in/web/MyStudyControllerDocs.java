package com.asyncsite.studyservice.membership.adapter.in.web;

import com.asyncsite.coreplatform.common.dto.ApiResponse;
import com.asyncsite.studyservice.membership.adapter.in.web.dto.MyApplicationResponse;
import com.asyncsite.studyservice.membership.adapter.in.web.dto.MyStudyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Tag(name = "My Study", description = "내 스터디 관련 API")
public interface MyStudyControllerDocs {

    @Operation(summary = "내 지원 현황", description = "내가 지원한 스터디 목록을 조회합니다")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ApiResponse<List<MyApplicationResponse>> getMyApplications(
            @Parameter(description = "사용자 ID") @RequestParam String userId);

    @Operation(summary = "내가 참여중인 스터디", description = "내가 멤버로 참여중인 스터디 목록을 조회합니다")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ApiResponse<List<MyStudyResponse>> getMyStudies(
            @Parameter(description = "사용자 ID") @RequestParam String userId);

    @Operation(summary = "내 스터디 대시보드", description = "내 스터디 활동 요약 정보를 조회합니다")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ApiResponse<Map<String, Object>> getMyDashboard(
            @Parameter(description = "사용자 ID") @RequestParam String userId);

    @Operation(summary = "추천 스터디", description = "사용자에게 추천하는 스터디 목록을 조회합니다")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ApiResponse<List<Map<String, Object>>> getRecommendations(
            @Parameter(description = "사용자 ID") @RequestParam String userId);
}