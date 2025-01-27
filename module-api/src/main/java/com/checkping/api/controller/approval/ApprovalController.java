package com.checkping.api.controller.approval;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.approval.ApprovalRegister;
import com.checkping.service.approval.ApprovalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/project/{projectId}/approval")
@RequiredArgsConstructor
public class ApprovalController implements ApprovalApi {

    private final ApprovalService approvalService;

    @PostMapping
    @Override
    public BaseResponse<ApprovalRegister.Response> register(
        @PathVariable Long projectId,
        @RequestBody ApprovalRegister.Request request) {

        ApprovalRegister.Response response = approvalService.register(projectId, request);

        return BaseResponse.success(response);
    }
}
