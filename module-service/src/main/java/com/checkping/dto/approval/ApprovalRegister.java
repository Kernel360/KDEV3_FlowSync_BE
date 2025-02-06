package com.checkping.dto.approval;

import com.checkping.common.utils.FileRequest;
import com.checkping.domain.approval.Approval;
import com.checkping.domain.member.Member;
import com.checkping.domain.project.ProgressStep;
import com.checkping.domain.project.Project;
import com.checkping.dto.approval.file.ApprovalFileRegister;
import com.checkping.dto.approval.link.ApprovalLinkRegister;
import com.checkping.dto.member.response.MemberResponseDto.MeResponseDto;
import com.checkping.dto.project.ProgressStepGet;
import com.checkping.exception.approval.ApprovalContentParsingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApprovalRegister {

    @Getter
    public static class Request {

        /*
        progress_step_id : 프로젝트 진행 단계 id
        title : 제목
        content : 내용
        fileInfoList : 첨부 파일
         */
        private Long progressStepId;
        private String title;
        private List<ApprovalContent> content;
        private List<FileRequest> fileInfoList;
        private List<ApprovalLinkRegister.Request> linkList;


        public static Approval toEntity(Project project, ProgressStep progressStep, Member register,
            Request request) {
            return Approval.generate(project, progressStep, register, request.title,
                request.jsonToString());
        }

        private String jsonToString() {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.writeValueAsString(this.content);
            } catch (JsonProcessingException e) {
                throw new ApprovalContentParsingException();
            }
        }
    }

    @Getter
    public static class Response {

        /*
        id : id
        projectId : 프로젝트 id
        progressStepId : 프로젝트 진행 단계 id
        title : 제목
        content : 내용
        status : 결재 상태
        registerId : 작성자 id
        registerName : 작성자 이름
        cancleAt : 취소 일자
        approverAt : 승인 일시
        approverId : 승인자 id
        approverName : 승인자 이름
        updatedAt : 수정 일시
        regAt : 작성 일시
        deletedYn : 삭제 여부
        fileInfoList : 결재 첨부 파일 리스트
         */
        private Long id;
        private Long projectId;
        private ProgressStepGet.Response progressStep;
        private String title;
        private List<ApprovalContent> content;
        private String status;
        private MeResponseDto register;
        private LocalDateTime cancelAt;
        private LocalDateTime approverAt;
        private Long approverId;
        private String approverName;
        private LocalDateTime updatedAt;
        private LocalDateTime regAt;
        private List<ApprovalFileRegister.Response> fileInfoList;
        private List<ApprovalLinkRegister.Response> linkList;

        public static Response toDto(Approval approval) {
            Response dto = new Response();
            dto.id = approval.getId();
            dto.projectId = approval.getProject().getId();
            dto.progressStep = ProgressStepGet.Response.toDto(approval.getProgressStep());
            dto.title = approval.getTitle();
            dto.content = ApprovalRegister.Response.stringToJson(approval.getContent());
            dto.status = approval.getStatus().name();
            dto.register = MeResponseDto.fromEntity(approval.getRegister());
            dto.cancelAt = approval.getCancelAt();
            dto.approverAt = approval.getApproverAt();
            dto.approverId = approval.getApproverId();
            dto.approverName = approval.getApproverName();
            dto.updatedAt = approval.getUpdatedAt();
            dto.regAt = approval.getRegAt();
            dto.fileInfoList = ApprovalFileRegister.Response.toDto(approval.getFileList());
            dto.linkList = ApprovalLinkRegister.Response.toDto(approval.getLinkList());
            return dto;
        }

        private static List<ApprovalContent> stringToJson(String content) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.readValue(content, new TypeReference<List<ApprovalContent>>() {
                });
            } catch (JsonProcessingException e) {
                throw new ApprovalContentParsingException();
            }
        }
    }


}
