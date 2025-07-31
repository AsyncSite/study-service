package com.asyncsite.studyservice.membership.adapter.in.web;

import com.asyncsite.coreplatform.common.dto.ApiResponse;
import com.asyncsite.studyservice.membership.adapter.in.web.dto.MyApplicationResponse;
import com.asyncsite.studyservice.membership.adapter.in.web.dto.MyStudyResponse;
import com.asyncsite.studyservice.membership.domain.model.Application;
import com.asyncsite.studyservice.membership.domain.model.Member;
import com.asyncsite.studyservice.membership.domain.port.in.MyStudyUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/my")
@Tag(name = "My Study", description = "내 스터디 관련 API")
@RequiredArgsConstructor
public class MyStudyController implements MyStudyControllerDocs {

    private final MyStudyUseCase myStudyUseCase;

    @Override
    @GetMapping("/applications")
    public ApiResponse<List<MyApplicationResponse>> getMyApplications(
            @RequestParam String userId) {
        List<Application> applications = myStudyUseCase.getMyApplications(userId);
        List<MyApplicationResponse> responseList = applications.stream()
                .map(app -> MyApplicationResponse.from(app, app.getStudyTitle()))
                .collect(java.util.stream.Collectors.toList());
        return ApiResponse.success(responseList);
    }

    @Override
    @GetMapping("/studies")
    public ApiResponse<List<MyStudyResponse>> getMyStudies(
            @RequestParam String userId) {
        List<Member> members = myStudyUseCase.getMyStudies(userId);
        List<MyStudyResponse> responseList = members.stream()
                .map(member -> MyStudyResponse.from(member, member.getStudyTitle()))
                .collect(java.util.stream.Collectors.toList());
        return ApiResponse.success(responseList);
    }

    @Override
    @GetMapping("/dashboard")
    public ApiResponse<Map<String, Object>> getMyDashboard(
            @RequestParam String userId) {
        return ApiResponse.success(myStudyUseCase.getMyDashboard(userId));
    }

    @Override
    @GetMapping("/recommendations")
    public ApiResponse<List<Map<String, Object>>> getRecommendations(
            @RequestParam String userId) {
        return ApiResponse.success(myStudyUseCase.getRecommendations(userId));
    }
}