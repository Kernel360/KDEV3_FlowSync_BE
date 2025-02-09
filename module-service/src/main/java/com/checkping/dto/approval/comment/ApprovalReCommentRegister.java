package com.checkping.dto.approval.comment;

import com.checkping.common.utils.DateTimeUtils;
import com.checkping.domain.approval.Approval;
import com.checkping.domain.approval.ApprovalComment;
import com.checkping.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApprovalReCommentRegister {

    @Getter
    public static class Request {
        /*
        content : 대댓글 내용
         */

        @Schema(description = "대댓글 내용")
        private String content;

        /**
         * 대댓글 생성 팩토리 메서드
         *
         * @param request       요청
         * @param approval      결재 Entity
         * @param parentComment 부모 댓글 Entity
         * @param register
         * @return 대댓글 Entity
         */
        public static ApprovalComment toEntity(Request request, Approval approval,
            ApprovalComment parentComment, Member register) {
            return ApprovalComment.generate(request.getContent(), approval, parentComment, register);
        }
    }

    @Getter
    public static class Response {
        /*
        id : 대댓글 ID
        content : 대댓글 내용
        regAt : 작성 일시
        editAt : 수정 일시
        parentId : 부모 댓글 ID
        isParent : 부모 댓글 여부
         */

        @Schema(description = "대댓글 ID")
        private Long id;
        @Schema(description = "대댓글 내용")
        private String content;
        @Schema(description = "대댓글 작성 일시")
        private String regAt;
        @Schema(description = "대댓글 수정 일시")
        private String editAt;
        @Schema(description = "부모 댓글 ID")
        private Long parentId;

        /**
         * 대댓글 응답 DTO 생성
         *
         * @param approvalComment 대댓글 Entity
         * @return 대댓글 응답 DTO
         */
        public static Response toDto(ApprovalComment approvalComment) {
            Response dto = new Response();
            dto.id = approvalComment.getId();
            dto.content = approvalComment.getContent();
            dto.regAt = DateTimeUtils.format(approvalComment.getRegAt());
            dto.editAt = DateTimeUtils.format(approvalComment.getUpdatedAt());
            dto.parentId = approvalComment.getParent().getId();
            return dto;
        }
    }
}
