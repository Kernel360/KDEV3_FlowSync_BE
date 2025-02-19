package com.checkping.service.approval;

import com.checkping.domain.approval.Approval;
import com.checkping.domain.approval.ApprovalComment;
import com.checkping.domain.approval.ApprovalCompleteHistory;
import com.checkping.domain.approval.ApprovalCompleteHistory.Status;
import com.checkping.domain.approval.ApprovalFile;
import com.checkping.domain.approval.ApprovalLink;
import com.checkping.domain.member.Member;
import com.checkping.domain.project.ProgressStep;
import com.checkping.domain.project.Project;
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
import com.checkping.dto.approval.comment.ApprovalCommentRegister.Request;
import com.checkping.dto.approval.comment.ApprovalCommentRegister.Response;
import com.checkping.dto.approval.comment.ApprovalCommentUpdate;
import com.checkping.dto.approval.comment.ApprovalReCommentRegister;
import com.checkping.dto.approval.file.ApprovalFileRegister;
import com.checkping.dto.approval.file.ApprovalFileUpdate;
import com.checkping.dto.approval.history.complete.ApprovalCompleteHistoryInfo;
import com.checkping.dto.approval.history.complete.ApprovalCompleteHistorySearch;
import com.checkping.dto.approval.link.ApprovalLinkRegister;
import com.checkping.dto.approval.link.ApprovalLinkUpdate;
import com.checkping.exception.approval.ApprovalNotFoundEntityException;
import com.checkping.exception.approval.comment.ApprovalCommentNotFoundEntityException;
import com.checkping.exception.approval.comment.ApprovalCommentNotRegisterException;
import com.checkping.exception.project.progressstep.ProgressStepMismatchProjectException;
import com.checkping.exception.project.progressstep.ProgressStepNotFoundException;
import com.checkping.info.approval.ApprovalCountProjection;
import com.checkping.info.approval.ApprovalSearchInfo;
import com.checkping.infra.repository.approval.ApprovalCompleteHistoryReader;
import com.checkping.infra.repository.approval.ApprovalCompleteHistoryStore;
import com.checkping.infra.repository.approval.ApprovalReader;
import com.checkping.infra.repository.approval.ApprovalStore;
import com.checkping.infra.repository.approval.comment.ApprovalCommentReader;
import com.checkping.infra.repository.approval.comment.ApprovalCommentStore;
import com.checkping.infra.repository.approval.file.ApprovalFileStore;
import com.checkping.infra.repository.approval.link.ApprovalLinkStore;
import com.checkping.infra.repository.project.ProgressStepReader;
import com.checkping.infra.repository.project.ProjectReader;
import com.checkping.service.member.util.CurrentMemberUtil;
import java.util.List;
import java.util.Objects;
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
    private final ProjectReader projectReader;
    private final ProgressStepReader progressStepReader;
    private final CurrentMemberUtil currentMemberUtil;
    private final ApprovalAuthorizationValidator approvalAuthorizationValidator;
    private final ApprovalCompleteHistoryStore approvalCompleteHistoryStore;
    private final ApprovalCompleteHistoryReader approvalCompleteHistoryReader;

    @Transactional
    @Override
    public ApprovalRegister.Response register(Long projectId, ApprovalRegister.Request request) {

        // Get Member From SecurityContext
        Member member = currentMemberUtil.getCurrentMember();

        // 등록 권한 확인
        approvalAuthorizationValidator.validateAllowedRegisterApproval(projectId, member);

        // Find project
        Project project = projectReader.getById(projectId);

        // Find progressStep
        ProgressStep progressStep = progressStepReader.getById(request.getProgressStepId())
            .orElseThrow(ProgressStepNotFoundException::new);

        // Check project match progressStep
        checkProjectContainsProgressStep(progressStep, project);

        Approval init = ApprovalRegister.Request.toEntity(project, progressStep, member, request);

        Approval approval = approvalStore.store(init);

        // FileRequest -> Entity
        List<ApprovalFile> approvalFiles = ApprovalFileRegister.Request.toEntity(approval,
            request.getFileInfoList());
        // Save approvalFiles
        approvalFileStore.store(approvalFiles);
        // Add approvalFiles to approval
        approval.updateFiles(approvalFiles);

        // LinkRequest -> Entity
        List<ApprovalLink> approvalLinks = ApprovalLinkRegister.Request.toEntity(approval,
            request.getLinkList());
        // Save approvalLinks
        approvalLinkStore.store(approvalLinks);
        // Add approvalLinks to approval
        approval.updateLinks(approvalLinks);

        // 진행 단계 완료 요청 결재 등록 시 진행 단계 완료 처리
        if (approval.isCompleteRequest()) {
            ApprovalCompleteHistory completeHistory = ApprovalCompleteHistoryInfo.toEntity(project,
                progressStep, approval, Status.CREATE, member);
            approvalCompleteHistoryStore.store(completeHistory);
        }

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

        // 어드민일 때 조회 권한 추가
        boolean adminSearch = false;

        // ApprovalSearchCondition -> ApprovalSearchInfo.SearchCondition
        ApprovalSearchInfo.SearchCondition searchCondition = ApprovalSearchCondition.toInfo(request,
            adminSearch);

        // Search Approval
        Page<Approval> approvals = approvalReader.getApprovals(projectId, searchCondition);

        // Approval Entity List -> ApprovalItem Dto List
        return ApprovalSearch.Response.toDto(approvals);
    }

    /**
     * 결재 상세 조회 부모 댓글과 자식 댓글도 같이 조회된다.
     *
     * @param projectId  프로젝트 아이디
     * @param approvalId 결재 아이디
     * @return 결재 상세 조회 결과
     */
    @Transactional(readOnly = true)
    @Override
    public ApprovalGet.Response get(Long projectId, Long approvalId) {

        // Get Member From SecurityContext
        Member member = currentMemberUtil.getCurrentMember();

        // check project contain approval
        approvalAuthorizationValidator.validateAccessibleApproval(projectId, approvalId, member);

        // find approval
        Approval approval = approvalReader.getByIdWithComments(projectId, approvalId)
            .orElseThrow(ApprovalNotFoundEntityException::new);

        // Entity -> Response
        return ApprovalGet.Response.toDto(approval);
    }

    @Transactional
    @Override
    public ApprovalUpdate.Response update(Long projectId, Long approvalId,
        ApprovalUpdate.Request request) {

        // Get Member From SecurityContext
        Member member = currentMemberUtil.getCurrentMember();

        // update 권한 확인
        approvalAuthorizationValidator.validateModifiableApproval(projectId, approvalId, member);

        // find approval
        Approval approval = approvalReader.getById(approvalId)
            .orElseThrow(ApprovalNotFoundEntityException::new);

        // update Approval
        approval.update(request.getTitle(), request.getContent());

        // 첨부 링크 처리
        // 1. approval 에 속한 파일 중에서 request 에 없는 것은 삭제 처리 한다.
        List<ApprovalLink> currentLinks = approval.getLinkList();
        List<ApprovalLinkUpdate.Request> requestLinks = request.getLinkList();

        for (ApprovalLink currentLink : currentLinks) {
            boolean isExist = requestLinks.stream()
                .anyMatch(requestLink -> Objects.equals(currentLink.getId(), requestLink.getId()));
            if (!isExist) {
                currentLink.deactivate();
            }
        }

        // 2. request 에서 id 가 없는 것들은 생성한다.
        List<ApprovalLink> newLinks = requestLinks.stream()
            .filter(requestLink -> requestLink.getId() == null)
            .map(requestLink -> ApprovalLinkUpdate.Request.toEntity(approval, requestLink))
            .toList();

        // Save approvalLinks
        approvalLinkStore.store(newLinks);

        // 첨부 파일 처리
        // 1. approval 에 속한 파일 중에서 request 에 없는 것은 삭제 처리 한다.
        List<ApprovalFile> currentFiles = approval.getFileList();
        List<ApprovalFileUpdate.Request> requestFiles = request.getFileInfoList();

        for (ApprovalFile currentFile : currentFiles) {
            boolean isExist = requestFiles.stream()
                .anyMatch(requestFile -> Objects.equals(currentFile.getId(), requestFile.getId()));
            if (!isExist) {
                currentFile.deactivate();
            }
        }

        // 2. request 에서 id 가 없는 것들은 생성한다.
        List<ApprovalFile> newFiles = requestFiles.stream()
            .filter(requestFile -> requestFile.getId() == null)
            .map(requestFile -> ApprovalFileUpdate.Request.toEntity(approval, requestFile))
            .toList();

        // Save approvalFiles
        approvalFileStore.store(newFiles);

        // 진행 단계 완료 요청 결재 등록 시 진행 단계 완료 처리
        if (approval.isCompleteRequest()) {
            ApprovalCompleteHistory completeHistory = ApprovalCompleteHistoryInfo.toEntity(approval,
                Status.MODIFY, member);
            approvalCompleteHistoryStore.store(completeHistory);
        }

        return ApprovalUpdate.Response.toDto(approval);
    }

    @Transactional
    @Override
    public ApprovalDelete.Response delete(Long projectId, Long approvalId) {

        // Get Member From SecurityContext
        Member member = currentMemberUtil.getCurrentMember();

        // delete 권한 확인
        approvalAuthorizationValidator.validateModifiableApproval(projectId, approvalId, member);

        // find approval
        Approval approval = approvalReader.getById(approvalId)
            .orElseThrow(ApprovalNotFoundEntityException::new);

        // delete approval
        approval.deactivate();

        // Approval Link deactivate
        List<ApprovalLink> approvalLinks = approval.getLinkList();
        for (ApprovalLink approvalLink : approvalLinks) {
            approvalLink.deactivate();
        }

        // Approval File deactivate
        List<ApprovalFile> approvalFiles = approval.getFileList();
        for (ApprovalFile approvalFile : approvalFiles) {
            approvalFile.deactivate();
        }

        // 진행 단계 완료 요청 결재 등록 시 진행 단계 완료 처리
        if (approval.isCompleteRequest()) {
            ApprovalCompleteHistory completeHistory = ApprovalCompleteHistoryInfo.toEntity(approval,
                Status.DELETE, member);
            approvalCompleteHistoryStore.store(completeHistory);
        }

        return ApprovalDelete.Response.toDto(approval);
    }

    @Transactional
    @Override
    public Response registerComment(Long projectId, Long approvalId, Request request) {

        // Get Member From SecurityContext
        Member member = currentMemberUtil.getCurrentMember();

        // 권한 검사
        approvalAuthorizationValidator.validateAccessibleApproval(projectId, approvalId, member);

        // find approval
        Approval approval = approvalReader.getById(approvalId)
            .orElseThrow(ApprovalNotFoundEntityException::new);

        // Request -> Entity
        ApprovalComment init = ApprovalCommentRegister.Request.toEntity(request, approval, member);

        // Save comment
        ApprovalComment comment = approvalCommentStore.store(init);

        // Entity -> Response
        return ApprovalCommentRegister.Response.toDto(comment);
    }

    @Transactional
    @Override
    public ApprovalReCommentRegister.Response registerReComment(Long projectId, Long approvalId,
        Long commentId, ApprovalReCommentRegister.Request request) {

        // Get Member From SecurityContext
        Member member = currentMemberUtil.getCurrentMember();

        // 권한 확인
        approvalAuthorizationValidator.validateAccessibleApprovalComment(projectId, approvalId,
            member, commentId);

        // Find approval
        Approval approval = approvalReader.getById(approvalId)
            .orElseThrow(ApprovalNotFoundEntityException::new);

        // Find parent comment
        ApprovalComment parentComment = approvalCommentReader.getById(commentId)
            .orElseThrow(ApprovalCommentNotFoundEntityException::new);

        // Request -> Entity
        ApprovalComment init = ApprovalReCommentRegister.Request.toEntity(request, approval,
            parentComment, member);

        // Save reComment
        ApprovalComment reComment = approvalCommentStore.store(init);

        // Entity -> Response
        return ApprovalReCommentRegister.Response.toDto(reComment);
    }

    @Transactional
    @Override
    public ApprovalConfirm.Response confirm(Long projectId, Long approvalId) {

        // Get Current Member Info
        Member member = currentMemberUtil.getCurrentMember();

        // confirm 권한 확인
        approvalAuthorizationValidator.validateApprovableApproval(projectId, approvalId, member);

        // find approval
        Approval approval = approvalReader.getById(approvalId)
            .orElseThrow(ApprovalNotFoundEntityException::new);

        // Confirm
        approval.confirm(member);

        // 진행 단계 완료 요청 결재 승인 시 진행 단계 완료 처리
        if (approval.isCompleteRequest()) {
            ProgressStep progressStep = approval.getProgressStep();
            progressStep.completeStep(approval);

            ApprovalCompleteHistory completeHistory = ApprovalCompleteHistoryInfo.toEntity(approval,
                Status.CONFIRM, member);
            approvalCompleteHistoryStore.store(completeHistory);
        }

        return ApprovalConfirm.Response.toDto(approval);
    }

    @Transactional
    @Override
    public ApprovalReject.Response reject(Long projectId, Long approvalId) {
        // 권한 처리 : 프로젝트의 고객사 오너 회원만 가능하다.

        // Get Current Member Info
        Member member = currentMemberUtil.getCurrentMember();

        // reject 권한 확인
        approvalAuthorizationValidator.validateApprovableApproval(projectId, approvalId, member);

        // find approval
        Approval approval = approvalReader.getById(approvalId)
            .orElseThrow(ApprovalNotFoundEntityException::new);

        // Reject
        approval.reject(member);

        // 진행 단계 완료 요청 결재 반려 시 진행 단계 완료 처리
        if (approval.isCompleteRequest()) {
            ProgressStep progressStep = approval.getProgressStep();
            progressStep.rejectStep(approval);

            ApprovalCompleteHistory completeHistory = ApprovalCompleteHistoryInfo.toEntity(approval,
                Status.REJECT, member);
            approvalCompleteHistoryStore.store(completeHistory);
        }

        // Entity -> Response
        return ApprovalReject.Response.toDto(approval);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ApprovalCount.Response> countByProgressStep(Long projectId) {

        // Approval count by progress step
        List<ApprovalCountProjection> queryResult = approvalReader.countByProgressStep(projectId);

        // Entity -> Response
        return ApprovalCount.Response.toDto(queryResult);
    }

    @Transactional
    @Override
    public ApprovalCommentUpdate.Response updateComment(Long projectId, Long approvalId,
        Long commentId, ApprovalCommentUpdate.Request request) {

        // Get Member From SecurityContext
        Member member = currentMemberUtil.getCurrentMember();

        // 결재 댓글 접근 권한 확인
        approvalAuthorizationValidator.validateAccessibleApprovalComment(projectId, approvalId,
            member, commentId);

        // find approval comment
        ApprovalComment approvalComment = approvalCommentReader.getById(commentId)
            .orElseThrow(ApprovalCommentNotFoundEntityException::new);

        // 결재 댓글 작성자 확인
        checkCommentRegister(member, approvalComment);

        // update comment
        approvalComment.updateContent(request.getContent());

        return ApprovalCommentUpdate.Response.toDto(approvalComment);
    }

    @Transactional
    @Override
    public ApprovalCommentDelete.Response deleteComment(Long projectId, Long approvalId,
        Long commentId) {

        // Get Member From SecurityContext
        Member member = currentMemberUtil.getCurrentMember();

        // 권한 체크
        approvalAuthorizationValidator.validateAccessibleApprovalComment(projectId, approvalId,
            member, commentId);

        // find approval comment
        ApprovalComment approvalComment = approvalCommentReader.getById(commentId)
            .orElseThrow(ApprovalCommentNotFoundEntityException::new);

        // 댓글 작성자 확인
        checkCommentRegister(member, approvalComment);

        // delete comment
        approvalComment.deactivate();

        // Entity -> Response
        return ApprovalCommentDelete.Response.toDto(approvalComment);
    }

    @Transactional(readOnly = true)
    @Override
    public ApprovalCompleteHistorySearch.Response searchCompleteHistory(Long projectId,
        ApprovalCompleteHistorySearch.Condition condition) {

        // Get Member From SecurityContext
        Member member = currentMemberUtil.getCurrentMember();

        // 권한 체크
        approvalAuthorizationValidator.validateAllowedRegisterApproval(projectId, member);

        // search complete history
        Page<ApprovalCompleteHistory> completeHistories = approvalCompleteHistoryReader.search(
            projectId, condition.toInfo());

        return ApprovalCompleteHistorySearch.Response.toDto(completeHistories);
    }

    /**
     * 진행 단계와 프로젝트 일치 여부 확인
     *
     * @param progressStep  진행 단계
     * @param targetProject 프로젝트
     */
    private void checkProjectContainsProgressStep(ProgressStep progressStep,
        Project targetProject) {
        if (!progressStep.getProjectId().equals(targetProject.getId())) {
            // throw exception
            throw new ProgressStepMismatchProjectException();
        }
    }

    /**
     * 댓글 등록자와 멤버가 일치하는지 확인
     *
     * @param member          멤버
     * @param approvalComment 댓글
     */
    private void checkCommentRegister(Member member, ApprovalComment approvalComment) {
        if (member.isAdmin()) {
            return;
        }

        if (approvalComment.getRegister().getId().equals(member.getId())) {
            // throw exception
            throw new ApprovalCommentNotRegisterException();
        }
    }
}
