package com.checkping.dto.approval.file;

import com.checkping.common.utils.FileRequest;
import com.checkping.domain.approval.Approval;
import com.checkping.domain.approval.ApprovalFile;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
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
        private long size;

        /**
         * ApprovalFileRegister.Request ->  Entity
         *
         * @param approval 결재 Entity
         * @param request  ApprovalFileRegister.Request
         * @return ApprovalFile Entity
         */
        public static ApprovalFile toEntity(Approval approval, FileRequest request) {
            return ApprovalFile.generate(approval, request.originalName(), request.saveName(),
                request.url(), request.size());
        }

        /**
         * ApprovalFileRegister.Request List -> Entity List
         *
         * @param approval 결재 Entity
         * @param requests ApprovalFileRegister.Request List
         * @return ApprovalFile Entity List
         */
        public static List<ApprovalFile> toEntity(Approval approval, List<FileRequest> requests) {
            // Check null or empty
            if (requests == null || requests.isEmpty()) {
                return List.of();
            }

            return requests.stream()
                .map(req -> ApprovalFileRegister.Request.toEntity(approval, req))
                .toList();
        }
    }

    @Getter
    public static class Response {

        private Long id;
        private Long projectId;
        private String name;
        private String url;

        /**
         * Entity -> ApprovalFileRegister.Response
         *
         * @param approvalFile ApprovalFile Entity
         * @return ApprovalFileRegister.Response
         */
        public static ApprovalFileRegister.Response toDto(ApprovalFile approvalFile) {
            ApprovalFileRegister.Response response = new ApprovalFileRegister.Response();
            response.id = approvalFile.getId();
            // TODO : project Entity 에서 가져오도록 변경 필요
            response.projectId = approvalFile.getApproval().getProjectId();
            response.name = approvalFile.getSaveName();
            response.url = approvalFile.getUrl();

            return response;
        }

        /**
         * Entity List -> ApprovalFileRegister.Response List
         *
         * @param approvalFiles Entity List
         * @return ApprovalFileRegister.Response List
         */
        public static List<ApprovalFileRegister.Response> toDto(List<ApprovalFile> approvalFiles) {
            return approvalFiles.stream()
                .map(ApprovalFileRegister.Response::toDto)
                .toList();
        }
    }
}
