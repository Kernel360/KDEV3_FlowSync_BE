package com.checkping.service.approval;

import com.checkping.domain.approval.Approval;
import com.checkping.domain.approval.ApprovalComment;
import com.checkping.domain.approval.ApprovalFile;
import com.checkping.domain.approval.ApprovalLink;
import com.checkping.domain.member.Member;
import com.checkping.dto.approval.ApprovalConfirm;
import com.checkping.dto.approval.ApprovalGet;
import com.checkping.dto.approval.ApprovalRegister;
import com.checkping.dto.approval.ApprovalSearch;
import com.checkping.dto.approval.ApprovalSearchCondition;
import com.checkping.dto.approval.comment.ApprovalCommentRegister;
import com.checkping.dto.approval.comment.ApprovalCommentRegister.Request;
import com.checkping.dto.approval.comment.ApprovalCommentRegister.Response;
import com.checkping.dto.approval.comment.ApprovalReCommentRegister;
import com.checkping.dto.approval.file.ApprovalFileRegister;
import com.checkping.dto.approval.link.ApprovalLinkRegister;
import com.checkping.exception.approval.ApprovalAuthorityException;
import com.checkping.exception.approval.ApprovalMismatchException;
import com.checkping.exception.approval.ApprovalNotFoundEntityException;
import com.checkping.exception.approval.comment.ApprovalCommentMismatchException;
import com.checkping.exception.approval.comment.ApprovalCommentNotFoundEntityException;
import com.checkping.info.approval.ApprovalSearchInfo;
import com.checkping.infra.repository.approval.ApprovalReader;
import com.checkping.infra.repository.approval.ApprovalStore;
import com.checkping.infra.repository.approval.comment.ApprovalCommentReader;
import com.checkping.infra.repository.approval.comment.ApprovalCommentStore;
import com.checkping.infra.repository.approval.file.ApprovalFileStore;
import com.checkping.infra.repository.approval.link.ApprovalLinkStore;
import com.checkping.infra.repository.project.ProjectReader;
import com.checkping.service.member.util.CurrentMemberUtil;
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
    private final ApprovalCommentReader approvalCommentReader;
    private final CurrentMemberUtil currentMemberUtil;
    private final ProjectReader projectReader;

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

    /**
     * 결재 상세 조회
     * 부모 댓글과 자식 댓글도 같이 조회된다.
     *
     * @param projectId  프로젝트 아이디
     * @param approvalId 결재 아이디
     * @return 결재 상세 조회 결과
     */
    @Transactional(readOnly = true)
    @Override
    public ApprovalGet.Response get(Long projectId, Long approvalId) {

        // check project contain approval
        checkProjectContainApproval(projectId, approvalId);

        // find approval
        Approval approval = approvalReader.getByIdWithComments(approvalId)
            .orElseThrow(ApprovalNotFoundEntityException::new);

        // Entity -> Response
        return ApprovalGet.Response.toDto(approval);
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

    @Transactional
    @Override
    public ApprovalReCommentRegister.Response registerReComment(Long projectId, Long approvalId,
        Long commentId, ApprovalReCommentRegister.Request request) {

        // Check project contain approval
        checkProjectContainApproval(projectId, approvalId);

        // Find approval
        Approval approval = approvalReader.getById(approvalId)
            .orElseThrow(ApprovalNotFoundEntityException::new);

        // Check approval contain comment
        checkApprovalContainComment(approval, commentId);

        // Find parent comment
        ApprovalComment parentComment = approvalCommentReader.getById(commentId)
            .orElseThrow(ApprovalCommentNotFoundEntityException::new);

        // Request -> Entity
        ApprovalComment init = ApprovalReCommentRegister.Request.toEntity(request,
            approval, parentComment);

        // Save reComment
        ApprovalComment reComment = approvalCommentStore.store(init);

        // Entity -> Response
        return ApprovalReCommentRegister.Response.toDto(reComment);
    }

    @Transactional
    @Override
    public ApprovalConfirm.Response confirm(Long projectId, Long approvalId,
        ApprovalConfirm.Request request) {

        // Get Current Member Info
        Member member = currentMemberUtil.getCurrentMember();

        // Check project contain approval
        checkProjectContainApproval(projectId, approvalId);

        // Check Member Authority
        if (!projectReader.isCustomerOwner(projectId, member.getId())) {
            // throw exception
            throw new ApprovalAuthorityException();
        }

        // find approval
        Approval approval = approvalReader.getById(approvalId)
            .orElseThrow(ApprovalNotFoundEntityException::new);

        // Confirm or Reject
        if (approval.getStatus() == Approval.ApprovalStatus.REJECTED) {
            approval.reject(member);
        }
        if (approval.getStatus() == Approval.ApprovalStatus.APPROVED) {
            approval.confirm(member);
        }

        return ApprovalConfirm.Response.toDto(approval);
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

    /**
     * 해당 결재에 댓글이 포함되어 있는지 확인
     *
     * @param approval  결재
     * @param commentId 댓글 아이디
     * @throws ApprovalMismatchException 결재 불일치 예외
     */
    private void checkApprovalContainComment(Approval approval, Long commentId) {
        if (!approvalCommentReader.isContainingComment(approval.getId(), commentId)) {
            // throw exception
            throw new ApprovalCommentMismatchException();
        }
    }
}
