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
import com.checkping.exception.approval.ApprovalCategoryException;
import com.checkping.exception.approval.ApprovalContentParsingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApprovalRegister {

    @Getter
    @Setter
    public static class Request {

        /*
        progress_step_id : 프로젝트 진행 단계 id
        title : 제목
        content : 내용
        category : 결재 유형
        fileInfoList : 첨부 파일
         */
        @NotNull(message = "프로젝트 진행 단계를 입력해주세요")
        private Long progressStepId;
        @NotEmpty(message = "제목을 입력해주세요.")
        @Size(max = 100, message = "제목은 100자 이하로 입력해주세요.")
        private String title;
        @NotEmpty(message = "결재 유형을 입력해주세요")
        private String category;
        private List<ApprovalContent> content;
        private List<FileRequest> fileInfoList;
        private List<ApprovalLinkRegister.Request> linkList;


        public static Approval toEntity(Project project, ProgressStep progressStep, Member register,
            Request request) {
            return Approval.generate(project, progressStep, convertCategory(request.category),
                register, request.title, request.jsonToString());
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
        category : 결재 유형
        registerId : 작성자 id
        registerName : 작성자 이름
        approverAt : 승인 일시
        approver : 승인자
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
        private String category;
        private MeResponseDto register;
        private LocalDateTime approverAt;
        private MeResponseDto approver;
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
            dto.category = approval.getCategory().name();
            dto.register = MeResponseDto.fromEntity(approval.getRegister());
            dto.approverAt = null;
            dto.approver = null;
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

    /**
     * Enum : ApprovalCategory 변환 함수
     *
     * @param value ApprovalCategory 로 변환할 문자열
     * @return ApprovalCategory
     * @throws ApprovalCategoryException ApprovalCategory 변환 실패
     */
    public static Approval.ApprovalCategory convertCategory(String value) {
        try {
            return Approval.ApprovalCategory.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApprovalCategoryException();
        }
    }


}
