package com.checkping.dto.approval.comment;

import com.checkping.domain.approval.ApprovalComment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApprovalCommentDelete {

    @Getter
    public static class Response {
        private Long commentId;

        public static Response toDto(ApprovalComment approvalComment) {
            Response dto = new Response();
            dto.commentId = approvalComment.getId();
            return dto;
        }
    }

}
