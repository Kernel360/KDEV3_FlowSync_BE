package com.checkping.dto.approval.comment;

import com.checkping.common.utils.DateTimeUtils;
import com.checkping.domain.approval.ApprovalComment;
import com.checkping.dto.member.response.MemberResponseDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApprovalCommentUpdate {

    @Getter
    public static class Request {
        /*
        content : 댓글 내용
         */
        private String content;
    }

    @Getter
    public static class Response {
        /*
        id : id
        approval : 결재(FK : approval_id)
        content : 댓글 내용
        reg_at : 작성 일시
        updated_at : 수정 일시
        deleted_yn : 삭제 여부
        parent : 부모 댓글
        register : 댓글 작성자
         */
        private Long id;
        private Long approvalId;
        private String content;
        private String regAt;
        private String updatedAt;
        private String deletedYn;
        private MemberResponseDto.MeResponseDto register;

        public static Response toDto(ApprovalComment approvalComment) {
            Response dto = new Response();
            dto.id = approvalComment.getId();
            dto.approvalId = approvalComment.getApproval().getId();
            dto.content = approvalComment.getContent();
            dto.regAt = DateTimeUtils.format(approvalComment.getRegAt());
            dto.updatedAt = DateTimeUtils.format(approvalComment.getUpdatedAt());
            dto.deletedYn = null;
            dto.register = MemberResponseDto.MeResponseDto.fromEntity(approvalComment.getRegister());
            return dto;
        }
    }

}
