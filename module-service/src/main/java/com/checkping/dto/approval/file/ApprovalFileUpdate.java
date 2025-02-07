package com.checkping.dto.approval.file;

import com.checkping.domain.approval.Approval;
import com.checkping.domain.approval.ApprovalFile;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApprovalFileUpdate {

    @Getter
    public static class Request {

        /*
        id: 결재 첨부 파일 ID
        originalName : 원본 파일명
        saveName : 저장 파일명
        url : 저장 URL
        size : 파일 Size
         */
        private Long id;
        private String originalName;
        private String saveName;
        private String url;
        private long size;


        /**
         * 결재 첨부 파일 엔티티로 변환하는 메서드
         *
         * @param approval 결재 엔티티
         * @param request  요청 정보
         * @return 결재 첨부 파일 엔티티
         */
        public static ApprovalFile toEntity(Approval approval, Request request) {
            return ApprovalFile.generate(approval, request.getOriginalName(), request.getSaveName(),
                request.getUrl(), request.getSize());
        }
    }


    @Getter
    public static class Response {

        /*
        id: 결재 첨부 파일 ID
        originalName : 원본 파일명
        saveName : 저장 파일명
        url : 저장 URL
        size : 파일 Size
        */
        private Long id;
        private Long approvalId;
        private String originalName;
        private String saveName;
        private String url;
        private long size;

        /**
         * 결재 첨부 파일 엔티티를 응답 정보로 변환하는 메서드
         *
         * @param file 결재 첨부 파일 엔티티
         * @return 결재 첨부 파일 응답 정보
         */
        public static Response toDto(ApprovalFile file) {
            Response response = new Response();
            response.id = file.getId();
            response.approvalId = file.getApproval().getId();
            response.originalName = file.getOriginalName();
            response.saveName = file.getSaveName();
            response.url = file.getUrl();
            response.size = file.getSize();
            return response;
        }

        /**
         * 결재 첨부 파일 엔티티 목록을 응답 정보 목록으로 변환하는 메서드
         *
         * @param files 결재 첨부 파일 엔티티 목록
         * @return 결재 첨부 파일 응답 정보 목록
         */
        public static List<Response> toDto(List<ApprovalFile> files) {
            return files.stream().map(Response::toDto).toList();
        }
    }

}
