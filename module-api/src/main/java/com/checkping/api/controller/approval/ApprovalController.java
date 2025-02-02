package com.checkping.api.controller.approval;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.approval.ApprovalRegister;
import com.checkping.dto.approval.ApprovalSearch;
import com.checkping.dto.approval.ApprovalSearchCondition;
import com.checkping.service.approval.ApprovalService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("/projects/{projectId}/approvals")
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

    @GetMapping
    @Override
    public BaseResponse<ApprovalSearch.Response> search(@PathVariable Long projectId,
        @RequestParam(required = false) Long progressId,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String keyword,
        @Min(0) @RequestParam(defaultValue = "1") Integer currentPage,
        @RequestParam(defaultValue = "10") Integer pageSize) {

        // Create ApprovalSearchCondition
        ApprovalSearchCondition request = new ApprovalSearchCondition(progressId, status, keyword,
            currentPage, pageSize);

        // Search Approval
        ApprovalSearch.Response response = approvalService.search(projectId, request);

        return BaseResponse.success(response);
    }
}
