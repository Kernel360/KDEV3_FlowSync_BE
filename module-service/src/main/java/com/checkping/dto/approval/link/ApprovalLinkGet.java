package com.checkping.dto.approval.link;

import com.checkping.domain.approval.ApprovalLink;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApprovalLinkGet {

    @Getter
    public static class Response {

        /*
        id : 결재 첨부 링크 ID
        name : 링크 이름
        url : url
         */
        private Long id;
        private String name;
        private String url;

        /**
         * 결재 첨부 링크 엔티티를 응답 정보로 변환하는 메서드
         *
         * @param approvalLink 결재 첨부 링크 엔티티
         * @return 결재 첨부 링크 응답 정보
         */
        public static Response toDto(ApprovalLink approvalLink) {
            Response response = new Response();
            response.id = approvalLink.getId();
            response.name = approvalLink.getLinkName();
            response.url = approvalLink.getLinkUrl();
            return response;
        }

        /**
         * 결재 첨부 파일 엔티티 리스트를 응답 정보 리스트로 변환하는 메서드
         *
         * @param approvalLinkList 결재 첨부 파일 엔티티 리스트
         * @return 결재 첨부 파일 응답 정보 리스트
         */
        public static List<Response> toDto(List<ApprovalLink> approvalLinkList) {
            return approvalLinkList.stream().map(Response::toDto).toList();
        }
    }

}
