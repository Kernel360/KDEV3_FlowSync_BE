package com.checkping.dto.approval;

import com.checkping.domain.approval.Approval;
import java.time.LocalDateTime;
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
         */
        private Long progressStepId;
        private String title;
        private String content;

        public static Approval toEntity(Long projectId, Long progressStepId, Long registerId, Request request) {
            // TODO: Member 에서 get 하도록 변경 필요
            String registerName = "TEST_NAME";
            return Approval.generate(projectId, progressStepId, registerId, registerName,
                request.title, request.content);
        }
    }

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
         */
        private Long id;
        private Long projectId;
        private Long progressStepId;
        private String title;
        private String content;
        private String status;
        private Long registerId;
        private String registerName;
        private LocalDateTime cancelAt;
        private LocalDateTime approverAt;
        private Long approverId;
        private String approverName;
        private LocalDateTime updatedAt;
        private LocalDateTime regAt;

        public static Response toDto(Approval approval) {
            Response dto = new Response();
            dto.id = approval.getId();
            dto.projectId = approval.getProjectId();
            dto.progressStepId = approval.getProgressStepId();
            dto.title = approval.getTitle();
            dto.content = approval.getContent();
            dto.status = approval.getStatus().name();
            dto.registerId = approval.getRegisterId();
            dto.registerName = approval.getRegisterName();
            dto.cancelAt = approval.getCancelAt();
            dto.approverAt = approval.getApproverAt();
            dto.approverId = approval.getApproverId();
            dto.approverName = approval.getApproverName();
            dto.updatedAt = approval.getUpdatedAt();
            dto.regAt = approval.getRegAt();
            return dto;
        }
    }


}
