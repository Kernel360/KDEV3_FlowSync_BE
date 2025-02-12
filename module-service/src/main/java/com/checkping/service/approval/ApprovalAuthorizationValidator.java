package com.checkping.service.approval;

import com.checkping.common.exception.BaseException;
import com.checkping.domain.member.Member;
import com.checkping.exception.approval.ApprovalMismatchProjectException;
import com.checkping.exception.approval.ApprovalRegisterAuthorityException;
import com.checkping.exception.approval.ApprovalUpdaterAuthorityException;
import com.checkping.exception.approval.comment.ApprovalCommentMismatchException;
import com.checkping.exception.project.ProjectNotCustomerOwnerException;
import com.checkping.exception.project.ProjectNotDevOwnerException;
import com.checkping.exception.project.ProjectNotMemberException;
import com.checkping.infra.repository.approval.ApprovalReader;
import com.checkping.infra.repository.approval.comment.ApprovalCommentReader;
import com.checkping.infra.repository.project.ProjectReader;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApprovalAuthorizationValidator {

    private final ProjectReader projectReader;
    private final ApprovalReader approvalReader;
    private final ApprovalCommentReader approvalCommentReader;


    /**
     * 결재 생성 권한 확인 - 프로젝트에 속한 회원
     *
     * @param projectId 프로젝트 id
     * @param member    멤버 엔티티
     */
    public void validateAllowedRegisterApproval(Long projectId, Member member) {
        // 관리자는 모든 행동 가능
        if (checkAdmin(member)) {
            return;
        }

        validate(checkProjectMember(projectId, member), ProjectNotMemberException::new);
    }

    /**
     * 결재 상세 조회 권한 확인 - 프로젝트에 속한 회원, 프로젝트에 속한 결재 글
     *
     * @param projectId  프로젝트 id
     * @param approvalId 결재 id
     * @param member     멤버 엔티티
     */
    public void validateAccessibleApproval(Long projectId, Long approvalId, Member member) {
        // 관리자는 모든 행동 가능
        if (checkAdmin(member)) {
            return;
        }

        validate(checkProjectMember(projectId, member), ProjectNotMemberException::new);
        validate(checkProjectContainsApproval(projectId, approvalId),
            ApprovalMismatchProjectException::new);
    }

    /**
     * 결재 수정 권한 확인 - 프로젝트에 속한 회원, 프로젝트에 속한 결재 글, 결재 등록자 또는 프로젝트 개발사 최고 담당자
     *
     * @param projectId  프로젝트 id
     * @param approvalId 결재 id
     * @param member     멤버 엔티티
     */
    public void validateModifiableApproval(Long projectId, Long approvalId, Member member) {
        // 관리자는 모든 행동 가능
        if (checkAdmin(member)) {
            return;
        }

        validate(checkProjectMember(projectId, member), ProjectNotMemberException::new);
        validate(checkProjectContainsApproval(projectId, approvalId),
            ApprovalMismatchProjectException::new);

        validate(
            checkApprovalRegister(approvalId, member) || checkProjectDevOwner(projectId, member),
            ApprovalUpdaterAuthorityException::new);
    }

    /**
     * 결재 삭제 권한 확인 - 프로젝트에 속한 회원, 프로젝트에 속한 결재 글, 고객사 최고 담당자
     *
     * @param projectId  프로젝트 id
     * @param approvalId 결재 id
     * @param member     멤버 엔티티
     */
    public void validateApprovableApproval(Long projectId, Long approvalId, Member member) {
        // 관리자는 모든 행동 가능
        if (checkAdmin(member)) {
            return;
        }

        validate(checkProjectMember(projectId, member), ProjectNotMemberException::new);
        validate(checkProjectContainsApproval(projectId, approvalId),
            ApprovalMismatchProjectException::new);
        validate(checkProjectCustomerOwner(projectId, member),
            ProjectNotCustomerOwnerException::new);
    }

    /**
     * 결재 댓글 접근 권한 확인 - 프로젝트에 속한 회원, 프로젝트에 속한 결재 글
     *
     * @param projectId  프로젝트 id
     * @param approvalId 결재 id
     * @param member     멤버 엔티티
     */
    public void validateAccessibleApprovalComment(Long projectId, Long approvalId, Member member) {
        // 관리자는 모든 행동 가능
        if (checkAdmin(member)) {
            return;
        }

        validate(checkProjectMember(projectId, member), ProjectNotMemberException::new);
        validate(checkProjectContainsApproval(projectId, approvalId),
            ApprovalMismatchProjectException::new);
        validate(checkApprovalContainsComment(approvalId, member.getId()),
            ApprovalCommentMismatchException::new);
    }


    /**
     * 유효성 검증 실행 코드 : 람다로 해당 조건을 실행하도록 하였음
     *
     * @param condition 조건 결과 : false 일 경우 예외 발생
     * @param exception 예외
     */
    private void validate(boolean condition, Supplier<? extends BaseException> exception) {
        if (!condition) {
            throw exception.get();
        }
    }

    /**
     * 프로젝트 관리자인지 확인
     *
     * @param member 멤버 엔티티
     * @return 프로젝트 관리자 여부
     */
    private boolean checkAdmin(Member member) {
        return member.isAdmin();
    }

    /**
     * 해당 프로젝트가 결재에 포함되어 있는지 확인
     *
     * @param projectId  프로젝트 id
     * @param approvalId 결재 id
     * @throws ApprovalMismatchProjectException 해당 프로젝트에 속한 결재 글이 아닌 경우
     */
    private boolean checkProjectContainsApproval(Long projectId, Long approvalId) {
        return approvalReader.existsByProjectIdAndId(projectId, approvalId);
    }

    /**
     * 프로젝트 멤버인지 확인
     *
     * @param projectId 프로젝트 id
     * @param member    멤버 엔티티
     * @throws ProjectNotMemberException 프로젝트 멤버가 아닌 경우
     */
    private boolean checkProjectMember(Long projectId, Member member) {
        return projectReader.matchProjectAndOrganization(projectId,
            member.getOrganization().getId());
    }

    /**
     * 프로젝트 고객사 최고 담당자인지 확인
     *
     * @param projectId 프로젝트 id
     * @param member    멤버 엔티티
     * @throws ProjectNotCustomerOwnerException 프로젝트 고객사 최고 담당자가 아닌 경우
     */
    private boolean checkProjectCustomerOwner(Long projectId, Member member) {
        return projectReader.isCustomerOwner(projectId, member.getId());
    }

    /**
     * 프로젝트 개발사 최고 담당자인지 확인
     *
     * @param projectId 프로젝트 id
     * @param member    멤버 엔티티
     * @throws ProjectNotDevOwnerException 프로젝트 개발사 최고 담당자가 아닌 경우
     */
    private boolean checkProjectDevOwner(Long projectId, Member member) {
        return projectReader.isDevOwner(projectId, member.getId());
    }

    /**
     * 결재 등록자인지 확인
     *
     * @param approvalId 결재 id
     * @param member     멤버 엔티티
     * @throws ApprovalRegisterAuthorityException 결재 등록자가 아닌 경우
     */
    private boolean checkApprovalRegister(Long approvalId, Member member) {
        return approvalReader.isApprovalRegister(approvalId, member.getId());
    }

    /**
     * 결재에 댓글이 포함되어 있는지 확인
     *
     * @param approvalId 결재 id
     * @param commentId  댓글 id
     * @return 댓글 포함 여부
     */
    private boolean checkApprovalContainsComment(Long approvalId, Long commentId) {
        return approvalCommentReader.isContainingComment(approvalId, commentId);
    }
}
