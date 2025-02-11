package com.checkping.dto.approval.comment;

import com.checkping.common.utils.DateTimeUtils;
import com.checkping.domain.approval.ApprovalComment;
import com.checkping.dto.member.response.MemberResponseDto.MeResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApprovalCommentGet {

    @Getter
    public static class Response {

        /*
        id : 결재 댓글 ID
        approvalId : 결재 ID
        content : 결재 댓글 내용
        register : 작성자
        regAt : 작성 일시
        parentId : 부모 댓글 Id
        isParent : 부모 댓글 여부
         */
        @Schema(description = "결재 댓글 ID")
        private Long id;
        @Schema(description = "결재 ID")
        private Long approvalId;
        @Schema(description = "결재 댓글 내용")
        private String content;
        @Schema(description = "작성자")
        private MeResponseDto register;
        @Schema(description = "작성 일시")
        private String regAt;
        @Schema(description = "부모 댓글 Id")
        private Long parentId;
        @Schema(description = "부모 댓글 여부")
        private boolean isParent;

        /**
         * 결재 댓글이 부모 댓글인지 여부를 반환하는 메서드
         *
         * @return 부모 댓글 여부
         */
        public boolean getIsParent() {
            return isParent;
        }

        /**
         * 결재 댓글 엔티티를 응답 정보로 변환하는 메서드
         *
         * @param comment 결재 댓글 엔티티
         * @return 결재 댓글 응답 정보
         */
        public static Response toDto(ApprovalComment comment) {
            Response response = new Response();
            response.id = comment.getId();
            response.approvalId = comment.getApproval().getId();
            response.content = comment.getContent();
            response.regAt = DateTimeUtils.format(comment.getRegAt());
            response.register = MeResponseDto.fromEntity(comment.getRegister());

            response.parentId = null;
            response.isParent = true;

            if (comment.getParent() != null) {
                response.parentId = comment.getParent().getId();
                response.isParent = false;
            }

            return response;
        }

        /**
         * 결재 댓글 엔티티 리스트를 응답 정보 리스트로 변환하는 메서드
         *
         * @param comments 결재 댓글 엔티티 리스트
         * @return 결재 댓글 응답 정보 리스트
         */
        public static List<Response> toDto(List<ApprovalComment> comments) {
            return comments.stream()
                .map(Response::toDto)
                .toList();
        }
    }

}
