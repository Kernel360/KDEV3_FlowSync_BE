package com.checkping.dto.approval;

import com.checkping.domain.approval.Approval;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApprovalRegister {

    public static class Request {

        /*
        progress_step_id : 프로젝트 진행 단계 id
        title : 제목
        content : 내용
         */
        private Long progressStepId;
        private String title;
        private String content;
    }

    public static Approval toEntity(Long projectId, Long registerId, Request request) {
        // TODO: Member 에서 get 하도록 변경 필요
        String registerName = "TEST_NAME";
        return Approval.generate(projectId, request.progressStepId, registerId, registerName,
            request.title, request.content);
    }
}
