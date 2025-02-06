package com.checkping.dto.approval.link;

import com.checkping.domain.approval.Approval;
import com.checkping.domain.approval.ApprovalLink;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApprovalLinkRegister {

    @Getter
    @Setter
    public static class Request {

        /*
        name : 링크 이름
        url : 링크 url
         */
        private String name;
        private String url;

        /**
         * Dto -> Entity 변환 메서드
         *
         * @param approval 결재 Entity
         * @param request  링크 등록 요청 Dto
         * @return ApprovalLink Entity
         */
        public static ApprovalLink toEntity(Approval approval, Request request) {
            return ApprovalLink.generate(approval, request.getName(), request.getUrl());
        }

        /**
         * Dto -> Entity List 변환 메서드
         *
         * @param approval 결재 Entity
         * @param requests 링크 등록 요청 Dto List
         * @return ApprovalLink Entity List
         */
        public static List<ApprovalLink> toEntity(Approval approval, List<Request> requests) {
            return requests.stream().map(request -> toEntity(approval, request)).toList();
        }
    }

    @Getter
    public static class Response {
        /*
        id : id
        projectId : 프로젝트 id
        name : 링크 이름
        url : 링크 url
         */

        private Long id;
        private Long projectId;
        private String name;
        private String url;

        /**
         * Entity -> Dto 변환 메서드
         *
         * @param approvalLink 링크 Entity
         * @return ApprovalLink Dto
         */
        public static Response toDto(ApprovalLink approvalLink) {
            Response response = new Response();
            response.id = approvalLink.getId();
            response.projectId = approvalLink.getApproval().getProject().getId();
            response.name = approvalLink.getLinkName();
            response.url = approvalLink.getLinkUrl();
            return response;
        }

        /**
         * Entity List -> Dto List 변환 메서드
         *
         * @param approvalLinks 링크 Entity List
         * @return ApprovalLink Dto List
         */
        public static List<Response> toDto(List<ApprovalLink> approvalLinks) {
            return approvalLinks.stream().map(Response::toDto).toList();
        }
    }
}
