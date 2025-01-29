package com.checkping.dto.approval.file;

import com.checkping.domain.approval.Approval;
import com.checkping.domain.approval.ApprovalFile;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApprovalFileRegister {

    public static class Request {
        /*
        originalName : 첨부 파일 원본 명
        saveName : 첨부 파일 저장 명
        url : 첨부 파일 URL
        size : 첨부 파일 용량
         */

        private String originalName;
        private String saveName;
        private String url;
        private String size;

        /**
         * ApprovalFileRegister.Request ->  Entity
         *
         * @param approval 결재 Entity
         * @param request  ApprovalFileRegister.Request
         * @return ApprovalFile Entity
         */
        public static ApprovalFile toEntity(Approval approval, Request request) {
            return ApprovalFile.generate(approval, request.originalName, request.saveName,
                request.url, request.size);
        }

        /**
         * ApprovalFileRegister.Request List -> Entity List
         *
         * @param approval 결재 Entity
         * @param requests ApprovalFileRegister.Request List
         * @return ApprovalFile Entity List
         */
        public static List<ApprovalFile> toEntity(Approval approval, List<Request> requests) {
            // Check null or empty
            if (requests == null || requests.isEmpty()) {
                return List.of();
            }

            return requests.stream()
                .map(req -> ApprovalFileRegister.Request.toEntity(approval, req))
                .toList();
        }
    }
}
