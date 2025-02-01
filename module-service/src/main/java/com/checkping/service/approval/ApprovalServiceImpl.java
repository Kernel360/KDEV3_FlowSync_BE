package com.checkping.service.approval;

import com.checkping.domain.approval.Approval;
import com.checkping.domain.approval.ApprovalComment;
import com.checkping.domain.approval.ApprovalFile;
import com.checkping.domain.approval.ApprovalLink;
import com.checkping.dto.approval.ApprovalRegister;
import com.checkping.dto.approval.ApprovalSearch;
import com.checkping.dto.approval.ApprovalSearchCondition;
import com.checkping.dto.approval.comment.ApprovalCommentRegister;
import com.checkping.dto.approval.comment.ApprovalCommentRegister.Request;
import com.checkping.dto.approval.comment.ApprovalCommentRegister.Response;
import com.checkping.dto.approval.file.ApprovalFileRegister;
import com.checkping.dto.approval.link.ApprovalLinkRegister;
import com.checkping.exception.approval.ApprovalMismatchException;
import com.checkping.exception.approval.ApprovalNotFoundEntityException;
import com.checkping.info.approval.ApprovalSearchInfo;
import com.checkping.infra.repository.approval.ApprovalReader;
import com.checkping.infra.repository.approval.ApprovalStore;
import com.checkping.infra.repository.approval.comment.ApprovalCommentStore;
import com.checkping.infra.repository.approval.file.ApprovalFileStore;
import com.checkping.infra.repository.approval.link.ApprovalLinkStore;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApprovalServiceImpl implements ApprovalService {

    private final ApprovalStore approvalStore;
    private final ApprovalFileStore approvalFileStore;
    private final ApprovalLinkStore approvalLinkStore;
    private final ApprovalReader approvalReader;
    private final ApprovalCommentStore approvalCommentStore;

    @Transactional
    @Override
    public ApprovalRegister.Response register(Long projectId, ApprovalRegister.Request request) {

        // TODO : registerId 는 시큐리티에서 가져오도록 변경 필요
        Long registerId = 123123L;

        Approval init = ApprovalRegister.Request.toEntity(projectId,
            registerId, request);

        Approval approval = approvalStore.store(init);

        // FileRequest -> Entity
        List<ApprovalFile> approvalFiles = ApprovalFileRegister.Request.toEntity(approval,
            request.getFileInfoList());
        // Save approvalFiles
        approvalFileStore.store(approvalFiles);
        // Add approvalFiles to approval
        approval.addFiles(approvalFiles);

        // LinkRequest -> Entity
        List<ApprovalLink> approvalLinks = ApprovalLinkRegister.Request.toEntity(approval,
            request.getLinkList());
        // Save approvalLinks
        approvalLinkStore.store(approvalLinks);
        // Add approvalLinks to approval
        approval.addLinks(approvalLinks);

        return ApprovalRegister.Response.toDto(approval);
    }

    /**
     * 결재 목록 조회
     *
     * @param request 결재 목록 조회 조건
     * @return 결재 목록 조회 결과
     */
    @Transactional(readOnly = true)
    public ApprovalSearch.Response search(Long projectId, ApprovalSearchCondition request) {

        // ApprovalSearchCondition -> ApprovalSearchInfo.SearchCondition
        ApprovalSearchInfo.SearchCondition searchCondition = ApprovalSearchCondition.toInfo(
            request);

        // Search Approval
        Page<Approval> approvals = approvalReader.getApprovals(projectId, searchCondition);

        // Approval Entity List -> ApprovalItem Dto List
        return ApprovalSearch.Response.toDto(approvals);
    }

    @Transactional
    @Override
    public Response registerComment(Long projectId, Long approvalId, Request request) {

        // check project contain approval
        checkProjectContainApproval(projectId, approvalId);

        // find approval
        Approval approval = approvalReader.getById(approvalId)
            .orElseThrow(ApprovalNotFoundEntityException::new);

        // Request -> Entity
        ApprovalComment init = ApprovalCommentRegister.Request.toEntity(request,
            approval);

        // Save comment
        ApprovalComment comment = approvalCommentStore.store(init);

        // Entity -> Response
        return ApprovalCommentRegister.Response.toDto(comment);
    }

    /**
     * 해당 프로젝트에 결재가 포함되어 있는지 확인
     *
     * @param projectId  프로젝트 아이디
     * @param approvalId 결재 아이디
     * @throws ApprovalMismatchException 결재 불일치 예외
     */
    private void checkProjectContainApproval(Long projectId, Long approvalId) {
        if (!approvalReader.isContainingApproval(projectId, approvalId)) {
            // throw exception
            throw new ApprovalMismatchException();
        }
    }
}
