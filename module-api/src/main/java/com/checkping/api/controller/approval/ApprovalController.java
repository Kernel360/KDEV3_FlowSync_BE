package com.checkping.api.controller.approval;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.approval.ApprovalConfirm;
import com.checkping.dto.approval.ApprovalCount;
import com.checkping.dto.approval.ApprovalDelete;
import com.checkping.dto.approval.ApprovalGet;
import com.checkping.dto.approval.ApprovalRegister;
import com.checkping.dto.approval.ApprovalReject;
import com.checkping.dto.approval.ApprovalSearch;
import com.checkping.dto.approval.ApprovalSearchCondition;
import com.checkping.dto.approval.ApprovalUpdate;
import com.checkping.dto.approval.comment.ApprovalCommentDelete;
import com.checkping.dto.approval.comment.ApprovalCommentRegister;
import com.checkping.dto.approval.comment.ApprovalCommentUpdate;
import com.checkping.dto.approval.comment.ApprovalReCommentRegister;
import com.checkping.dto.approval.history.complete.ApprovalCompleteHistorySearch;
import com.checkping.service.approval.ApprovalService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public BaseResponse<ApprovalRegister.Response> register(@PathVariable Long projectId,
        @RequestBody @Valid ApprovalRegister.Request request) {

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

    @GetMapping("/{approvalId}")
    @Override
    public BaseResponse<ApprovalGet.Response> get(@PathVariable Long projectId,
        @PathVariable Long approvalId) {

        ApprovalGet.Response response = approvalService.get(projectId, approvalId);

        return BaseResponse.success(response);
    }

    @PutMapping("/{approvalId}")
    @Override
    public BaseResponse<ApprovalUpdate.Response> update(@PathVariable Long projectId,
        @PathVariable Long approvalId, @RequestBody ApprovalUpdate.Request request) {

        ApprovalUpdate.Response response = approvalService.update(projectId, approvalId, request);

        return BaseResponse.success(response);
    }

    @DeleteMapping("/{approvalId}")
    @Override
    public BaseResponse<ApprovalDelete.Response> delete(@PathVariable Long projectId,
        @PathVariable Long approvalId) {

        ApprovalDelete.Response response = approvalService.delete(projectId, approvalId);

        return BaseResponse.success(response);
    }

    @PostMapping("/{approvalId}/comments")
    @Override
    public BaseResponse<ApprovalCommentRegister.Response> registerComment(
        @PathVariable Long projectId, @PathVariable Long approvalId,
        @RequestBody @Valid ApprovalCommentRegister.Request request) {

        ApprovalCommentRegister.Response response = approvalService.registerComment(projectId,
            approvalId, request);

        return BaseResponse.success(response);
    }

    @PostMapping("/{approvalId}/comments/{commentId}/recomments")
    @Override
    public BaseResponse<ApprovalReCommentRegister.Response> registerReComment(
        @PathVariable Long projectId, @PathVariable Long approvalId, @PathVariable Long commentId,
        @RequestBody @Valid ApprovalReCommentRegister.Request request) {

        ApprovalReCommentRegister.Response response = approvalService.registerReComment(projectId,
            approvalId, commentId, request);

        return BaseResponse.success(response);
    }

    @PostMapping("/{approvalId}/confirm")
    @Override
    public BaseResponse<ApprovalConfirm.Response> confirm(@PathVariable Long projectId,
        @PathVariable Long approvalId) {

        // Confirm Approval
        ApprovalConfirm.Response response = approvalService.confirm(projectId, approvalId);

        return BaseResponse.success(response);
    }

    @PostMapping("/{approvalId}/reject")
    @Override
    public BaseResponse<ApprovalReject.Response> reject(@PathVariable Long projectId,
        @PathVariable Long approvalId) {

        ApprovalReject.Response response = approvalService.reject(projectId, approvalId);

        return BaseResponse.success(response);
    }

    @GetMapping("/counts")
    @Override
    public BaseResponse<List<ApprovalCount.Response>> countByProgressStep(
        @PathVariable Long projectId) {

        List<ApprovalCount.Response> response = approvalService.countByProgressStep(projectId);

        return BaseResponse.success(response);
    }

    @PutMapping("/{approvalId}/comments/{commentId}")
    @Override
    public BaseResponse<ApprovalCommentUpdate.Response> updateComment(@PathVariable Long projectId,
        @PathVariable Long approvalId, @PathVariable Long commentId,
        @RequestBody ApprovalCommentUpdate.Request request) {

        ApprovalCommentUpdate.Response response = approvalService.updateComment(projectId,
            approvalId, commentId, request);

        return BaseResponse.success(response);
    }

    @DeleteMapping("/{approvalId}/comments/{commentId}")
    @Override
    public BaseResponse<ApprovalCommentDelete.Response> deleteComment(@PathVariable Long projectId,
        @PathVariable Long approvalId, @PathVariable Long commentId) {

        ApprovalCommentDelete.Response response = approvalService.deleteComment(projectId,
            approvalId, commentId);

        return BaseResponse.success(response);
    }

    @GetMapping("/histories/completion-requests")
    @Override
    public BaseResponse<ApprovalCompleteHistorySearch.Response> searchCompleteHistory(
        @PathVariable Long projectId,
        @ModelAttribute @Valid ApprovalCompleteHistorySearch.Condition condition) {

        ApprovalCompleteHistorySearch.Response response = approvalService.searchCompleteHistory(
            projectId, condition);

        return BaseResponse.success(response);
    }
}
