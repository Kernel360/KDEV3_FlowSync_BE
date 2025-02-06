package com.checkping.dto.approval;

import com.checkping.common.utils.DateTimeUtils;
import com.checkping.domain.approval.Approval;
import com.checkping.domain.approval.Approval.ApprovalStatus;
import com.checkping.dto.approval.comment.ApprovalCommentGet;
import com.checkping.dto.approval.file.ApprovalFileGet;
import com.checkping.dto.approval.link.ApprovalLinkGet;
import com.checkping.dto.member.response.MemberResponseDto.MeResponseDto;
import com.checkping.dto.project.ProgressStepGet;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApprovalUpdate {

    @Getter
    public static class Request {
        /*
        title : 제목
        content : 내용
        fileInfoList : 첨부 파일
        linkList : 링크
         */

        private String title;
        private List<ApprovalContent> content;
        private List<ApprovalFileGet.Response> fileInfoList;
        private List<ApprovalLinkGet.Response> linkList;
    }

    @Getter
    public static class Response {
        /*
        id : 결재 ID
        projectId : 프로젝트 ID
        progressStepId : 프로젝트 진행 단계 ID
        title : 결재 제목
        content : 결재 내용
        status : 결재 상태
        registerId : 작성자 id
        registerName : 작성자 이름
        cancleAt : 취소 일자
        approverAt : 승인 일시
        approver : 승인자
        updatedAt : 수정 일시
        regAt : 작성 일시
        commentList : 결재 댓글 목록
        linkList : 결재 첨부 링크 목록
        fileList : 결재 첨부 파일 목록
         */
        @Schema(description = "결재 ID")
        private Long id;
        @Schema(description = "프로젝트 ID")
        private Long projectId;
        @Schema(description = "프로젝트 진행 단계")
        private ProgressStepGet.Response progressStep;
        @Schema(description = "결재 제목")
        private String title;
        @Schema(description = "결재 내용")
        private List<ApprovalContent> content;
        @Schema(description = "결재 상태")
        private ApprovalStatus status;
        @Schema(description = "작성자")
        private MeResponseDto register;
        @Schema(description = "취소 일자")
        private String cancelAt;
        @Schema(description = "승인 일시")
        private String approverAt;
        @Schema(description = "승인자")
        private MeResponseDto approver;
        @Schema(description = "수정 일시")
        private String updatedAt;
        @Schema(description = "작성 일시")
        private String regAt;
        @Schema(description = "결재 댓글 목록")
        private List<ApprovalCommentGet.Response> commentList;
        @Schema(description = "결재 첨부 링크 목록")
        private List<ApprovalLinkGet.Response> linkList;
        @Schema(description = "결재 첨부 파일 목록")
        private List<ApprovalFileGet.Response> fileList;

        /**
         * 결재 엔티티를 수정 응답 정보로 변환하는 메서드
         *
         * @param approval 결재 엔티티
         * @return 결재 응답 정보
         */
        public static ApprovalUpdate.Response toDto(Approval approval) {
            ApprovalUpdate.Response dto = new ApprovalUpdate.Response();
            dto.id = approval.getId();
            dto.projectId = approval.getProject().getId();
            dto.progressStep = ProgressStepGet.Response.toDto(approval.getProgressStep());
            dto.title = approval.getTitle();
            dto.content = ApprovalContent.toContentList(approval.getContent());
            dto.status = approval.getStatus();
            dto.register = MeResponseDto.fromEntity(approval.getRegister());
            dto.cancelAt = DateTimeUtils.format(approval.getCancelAt());
            dto.approverAt = DateTimeUtils.format(approval.getApproverAt());
            dto.approver = MeResponseDto.fromEntity(approval.getApprover());
            dto.updatedAt = DateTimeUtils.format(approval.getUpdatedAt());
            dto.regAt = DateTimeUtils.format(approval.getRegAt());
            dto.commentList = ApprovalCommentGet.Response.toDto(approval.getCommentList());
            dto.linkList = ApprovalLinkGet.Response.toDto(approval.getLinkList());
            dto.fileList = ApprovalFileGet.Response.toDto(approval.getFileList());
            return dto;
        }
    }

}
