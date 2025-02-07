package com.checkping.service.approval;

import com.checkping.domain.approval.Approval;
import com.checkping.domain.approval.ApprovalComment;
import com.checkping.domain.approval.ApprovalFile;
import com.checkping.domain.approval.ApprovalLink;
import com.checkping.domain.member.Member;
import com.checkping.domain.project.ProgressStep;
import com.checkping.domain.project.Project;
import com.checkping.dto.approval.ApprovalConfirm;
import com.checkping.dto.approval.ApprovalGet;
import com.checkping.dto.approval.ApprovalRegister;
import com.checkping.dto.approval.ApprovalSearch;
import com.checkping.dto.approval.ApprovalSearchCondition;
import com.checkping.dto.approval.ApprovalUpdate;
import com.checkping.dto.approval.comment.ApprovalCommentRegister;
import com.checkping.dto.approval.comment.ApprovalCommentRegister.Request;
import com.checkping.dto.approval.comment.ApprovalCommentRegister.Response;
import com.checkping.dto.approval.comment.ApprovalReCommentRegister;
import com.checkping.dto.approval.file.ApprovalFileGet;
import com.checkping.dto.approval.file.ApprovalFileRegister;
import com.checkping.dto.approval.link.ApprovalLinkRegister;
import com.checkping.dto.approval.link.ApprovalLinkUpdate;
import com.checkping.exception.approval.ApprovalAuthorityException;
import com.checkping.exception.approval.ApprovalMismatchException;
import com.checkping.exception.approval.ApprovalNotFoundEntityException;
import com.checkping.exception.approval.comment.ApprovalCommentMismatchException;
import com.checkping.exception.approval.comment.ApprovalCommentNotFoundEntityException;
import com.checkping.exception.project.progressstep.ProgressStepMismatchProjectException;
import com.checkping.exception.project.progressstep.ProgressStepNotFoundException;
import com.checkping.info.approval.ApprovalSearchInfo;
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

    @Transactional
    @Override
    public ApprovalRegister.Response register(Long projectId, ApprovalRegister.Request request) {

        // Get Member From SecurityContext
        Member member = currentMemberUtil.getCurrentMember();

        // Find project
        Project project = projectReader.getById(projectId);

        // Find progressStep
        ProgressStep progressStep = progressStepReader.getById(request.getProgressStepId())
            .orElseThrow(ProgressStepNotFoundException::new);

        // Check project match progressStep
        checkProgressStepMatchProject(progressStep, project);

        Approval init = ApprovalRegister.Request.toEntity(project, progressStep, member, request);

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
        approval.updateLinks(approvalLinks);

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
     * 결재 상세 조회 부모 댓글과 자식 댓글도 같이 조회된다.
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
    public ApprovalUpdate.Response update(Long projectId, Long approvalId,
        ApprovalUpdate.Request request) {

        // Get Member From SecurityContext
        Member member = currentMemberUtil.getCurrentMember();

        // check project contain approval
        checkProjectContainApproval(projectId, approvalId);

        // find approval
        Approval approval = approvalReader.getById(approvalId)
            .orElseThrow(ApprovalNotFoundEntityException::new);

        // Check Member Authority
        checkMemberAuthority(member, approval);

        // update Approval
        approval.update(request.getTitle(), request.getContent());

        // 첨부 파일 처리
        // request 와 비교해서 id 가 있는 것들을 제외하고 비활성화 처리
        List<ApprovalFile> existFiles = approval.getFileList();
        List<ApprovalFileGet.Response> requestFiles = request.getFileInfoList();

        // PUT 으로 안오는 것들 비활성화 처리
        for (ApprovalFile approvalFile : existFiles) {
            boolean isExist = requestFiles.stream()
                .anyMatch(file -> approvalFile.getId().equals(file.getId()));
            if (!isExist) {
                approvalFile.deactivate();
            }
        }

        // request 에서 파일 추가된 것들은 추가
        // FileRequest -> Entity
        List<ApprovalFile> initFiles = ApprovalFileRegister.Request.toEntity(approval,
            request.getFileRequests());
        // Save approvalFiles
        approvalFileStore.store(initFiles);
        // Add approvalFiles to approval
        approval.addFiles(initFiles);

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
        // Add approvalLinks to approval
        approval.updateLinks(newLinks);

        return ApprovalUpdate.Response.toDto(approval);
    }

    @Transactional
    @Override
    public Response registerComment(Long projectId, Long approvalId, Request request) {

        // Get Member From SecurityContext
        Member member = currentMemberUtil.getCurrentMember();

        // check project contain approval
        checkProjectContainApproval(projectId, approvalId);

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
        ApprovalComment init = ApprovalReCommentRegister.Request.toEntity(request, approval,
            parentComment, member);

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

    /**
     * 진행 단계와 프로젝트 일치 여부 확인
     *
     * @param progressStep  진행 단계
     * @param targetProject 프로젝트
     */
    private void checkProgressStepMatchProject(ProgressStep progressStep, Project targetProject) {
        if (!progressStep.getProjectId().equals(targetProject.getId())) {
            // throw exception
            throw new ProgressStepMismatchProjectException();
        }
    }

    /**
     * 결재 작성자 권한 확인 ADMIN 권한이거나 결재 작성자와 같은 업체이면 통과 그 외에는 예외 발생
     *
     * @param member   현재 사용자
     * @param approval 결재 Entity
     * @throws ApprovalAuthorityException 결재 권한 예외
     */
    private void checkMemberAuthority(Member member, Approval approval) {

        // Check Admin
        if (member.isAdmin()) {
            return;
        }

        Member register = approval.getRegister();
        // Check Register Organization
        if (!member.getOrganization().getId().equals(register.getOrganization().getId())) {
            // throw exception
            throw new ApprovalAuthorityException();
        }
    }
}
