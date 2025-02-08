package com.checkping.dto.approval;

import com.checkping.common.utils.DateTimeUtils;
import com.checkping.common.utils.FileRequest;
import com.checkping.domain.approval.Approval;
import com.checkping.domain.approval.Approval.ApprovalStatus;
import com.checkping.dto.approval.comment.ApprovalCommentGet;
import com.checkping.dto.approval.file.ApprovalFileGet;
import com.checkping.dto.approval.file.ApprovalFileUpdate;
import com.checkping.dto.approval.file.ApprovalFileUpdate.Request;
import com.checkping.dto.approval.link.ApprovalLinkGet;
import com.checkping.dto.approval.link.ApprovalLinkUpdate;
import com.checkping.dto.member.response.MemberResponseDto.MeResponseDto;
import com.checkping.dto.project.ProgressStepGet;
import com.checkping.exception.approval.ApprovalContentParsingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApprovalUpdate {

    @Getter
    public static class Request {
        /*
        title : 제목
        content : 내용
        fileInfoList : 첨부 파일
        linkList : 링크
         */

        private String title;
        private List<ApprovalContent> content;
        private List<ApprovalFileUpdate.Request> fileInfoList;
        private List<ApprovalLinkUpdate.Request> linkList;

        public Request(String title, List<ApprovalContent> content,
            List<ApprovalFileUpdate.Request> fileInfoList,
            List<ApprovalLinkUpdate.Request> linkList) {
            this.title = title;
            this.content = content;
            this.fileInfoList = fileInfoList == null ? new ArrayList<>() : fileInfoList;
            this.linkList = linkList == null ? new ArrayList<>() : linkList;
        }

        public String getContent() {
            return jsonToString(this.content);
        }

        /**
         * 결재 내용을 JSON 문자열로 변환하는 메서드
         *
         * @param content 결재 내용
         * @return JSON 문자열
         */
        private String jsonToString(List<ApprovalContent> content) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.writeValueAsString(content);
            } catch (JsonProcessingException e) {
                throw new ApprovalContentParsingException();
            }
        }

        /**
         * 수정할 결재 첨부파일을 FileRequest 리스트로 변환하는 메서드
         *
         * @return List<FileRequest>
         */
        public List<FileRequest> getFileRequests() {
            List<FileRequest> list = new ArrayList<>();
            for (ApprovalFileUpdate.Request file : fileInfoList) {
                FileRequest fileRequest = new FileRequest(file.getOriginalName(),
                    file.getSaveName(), file.getUrl(), file.getSize());
                list.add(fileRequest);
            }
            return list;
        }
    }

    @Getter
    public static class Response {

        /*
        id : 결재 ID
        projectId : 프로젝트 ID
        progressStepId : 프로젝트 진행 단계 ID
        result : 수정 결과
         */
        @Schema(description = "결재 ID")
        private Long id;
        @Schema(description = "프로젝트 ID")
        private Long projectId;
        @Schema(description = "프로젝트 진행 단계")
        private ProgressStepGet.Response progressStep;

        /**
         * 결재 엔티티를 수정 응답 정보로 변환하는 메서드
         *
         * @param approval 결재 엔티티
         * @return 결재 응답 정보
         */
        public static ApprovalUpdate.Response toDto(Approval approval) {
            ApprovalUpdate.Response dto = new ApprovalUpdate.Response();
            dto.id = approval.getId();
            dto.projectId = approval.getProject().getId();
            dto.progressStep = ProgressStepGet.Response.toDto(approval.getProgressStep());

            return dto;
        }
    }

}
