package com.checkping.dto.approval;

import com.checkping.common.utils.DateTimeUtils;
import com.checkping.domain.approval.Approval;
import com.checkping.domain.approval.Approval.ApprovalStatus;
import com.checkping.dto.approval.comment.ApprovalCommentGet;
import com.checkping.dto.approval.file.ApprovalFileGet;
import com.checkping.dto.approval.link.ApprovalLinkGet;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApprovalGet {

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
        approverId : 승인자 id
        approverName : 승인자 이름
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
        @Schema(description = "프로젝트 진행 단계 ID")
        private Long progressStepId;
        @Schema(description = "결재 제목")
        private String title;
        @Schema(description = "결재 내용")
        private List<ApprovalContent> content;
        @Schema(description = "결재 상태")
        private ApprovalStatus status;
        @Schema(description = "작성자 id")
        private Long registerId;
        @Schema(description = "작성자 이름")
        private String registerName;
        @Schema(description = "취소 일자")
        private String cancelAt;
        @Schema(description = "승인 일시")
        private String approverAt;
        @Schema(description = "승인자 id")
        private Long approverId;
        @Schema(description = "승인자 이름")
        private String approverName;
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
         * 결재 엔티티를 응답 정보로 변환하는 메서드
         *
         * @param approval 결재 엔티티
         * @return 결재 응답 정보
         */
        public static Response toDto(Approval approval) {
            Response response = new Response();
            response.id = approval.getId();
            response.projectId = approval.getProject().getId();
            response.progressStepId = approval.getProgressStepId();
            response.title = approval.getTitle();
            response.content = ApprovalContent.toContentList(approval.getContent());
            response.status = approval.getStatus();
            response.registerId = approval.getRegisterId();
            response.registerName = approval.getRegisterName();
            response.cancelAt = DateTimeUtils.format(approval.getCancelAt());
            response.approverAt = DateTimeUtils.format(approval.getApproverAt());
            response.approverId = approval.getApproverId();
            response.approverName = approval.getApproverName();
            response.updatedAt = DateTimeUtils.format(approval.getUpdatedAt());
            response.regAt = DateTimeUtils.format(approval.getRegAt());
            response.commentList = ApprovalCommentGet.Response.toDto(approval.getCommentList());
            response.linkList = ApprovalLinkGet.Response.toDto(approval.getLinkList());
            response.fileList = ApprovalFileGet.Response.toDto(approval.getFileList());
            return response;
        }
    }

}
