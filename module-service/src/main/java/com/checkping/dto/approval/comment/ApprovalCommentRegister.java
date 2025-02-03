package com.checkping.dto.approval.comment;

import com.checkping.common.utils.DateTimeUtils;
import com.checkping.domain.approval.Approval;
import com.checkping.domain.approval.ApprovalComment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApprovalCommentRegister {

    @Getter
    public static class Request {
        /*
        content : 댓글 내용
         */

        @Schema(description = "결재 댓글 내용")
        private String content;

        /**
         * Request -> Entity
         *
         * @param request   등록 정보
         * @param approval  결재 Entity
         * @return  ApprovalComment 엔티티
         */
        public static ApprovalComment toEntity(Request request, Approval approval) {
            return ApprovalComment.generate(request.getContent(), approval);
        }
    }

    @Getter
    public static class Response {
        /*
        id : 업무 관리 게시글 댓글 아이디
        content : 댓글 내용
        regAt : 작성 일시
        editAt : 수정 일시
         */

        @Schema(description = "결재 댓글 아이디")
        private Long id;
        @Schema(description = "결재 댓글 내용")
        private String content;
        @Schema(description = "결재 댓글 작성 일시")
        private String regAt;
        @Schema(description = "결재 댓글 수정 일시")
        private String editAt;

        /**
         * Entity -> Response (Dto)
         *
         * @param approvalComment   결재 댓글 Entity
         * @return  Response (Dto)
         */
        public static Response toDto(ApprovalComment approvalComment) {
            Response response = new Response();
            response.id = approvalComment.getId();
            response.content = approvalComment.getContent();
            response.regAt = DateTimeUtils.format(approvalComment.getRegAt());
            response.editAt = DateTimeUtils.format(approvalComment.getUpdatedAt());
            return response;
        }
    }
}
