package com.checkping.dto.project;

import com.checkping.common.utils.DateTimeUtils;
import com.checkping.domain.project.ProgressStep;
import com.checkping.dto.member.response.MemberResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgressStepUpdater {

    @Getter
    public static class Request {

        /*
        title : 진행 단계 제목
        description : 진행 단계 설명
        color : 진행 단계 색상
         */
        @NotEmpty(message = "제목을 입력해주세요.")
        private String title;
        @NotEmpty(message = "설명을 입력해주세요.")
        @Size(max = 1000, message = "설명은 1000자 이내로 입력해주세요.")
        private String description;
        private String color;
    }

    @Getter
    public static class Response {

        /*
        id : 진행 단계 ID
        name : 진행 단계 제목
        description : 진행 단계 설명
        color : 진행 단계 색상
        stepOrder : 진행 단계 순서
        status : 진행 단계 상태
        startAt : 진행 단계 시작 일시
        closeAt : 진행 단계 마감 일시
        deadlineAt : 진행 단계 예상 마감 일시
        projectId : 프로젝트 ID
        relatedApprovalId : 관련 결재 ID
        approver : 결재 승인자
         */
        @Schema(description = "진행 단계 ID")
        private Long id;
        @Schema(description = "단계명")
        private String name;
        @Schema(description = "단계 설명")
        private String description;
        @Schema(description = "커스텀 색상")
        private String color;
        @Schema(description = "순서")
        private Integer stepOrder;
        @Schema(description = "단계 상태")
        private String status;
        @Schema(description = "시작 일시")
        private String startAt;
        @Schema(description = "마감 일시")
        private String closeAt;
        @Schema(description = "예상 마감 일시")
        private String deadlineAt;
        @Schema(description = "프로젝트 ID")
        private Long projectId;
        @Schema(description = "관련 결재 ID")
        private Long relatedApprovalId = null;
        @Schema(description = "결재 승인자")
        private MemberResponseDto.MeResponseDto approver;

        /**
         * ProgressStep Entity -> ProgressStepGet.Response Dto
         *
         * @param progressStep ProgressStep Entity
         * @return ProgressStepGet.Response Dto
         */
        public static ProgressStepUpdater.Response toDto(ProgressStep progressStep) {
            ProgressStepUpdater.Response dto = new ProgressStepUpdater.Response();
            dto.id = progressStep.getId();
            dto.name = progressStep.getName();
            dto.description = progressStep.getDescription();
            dto.color = progressStep.getColor();
            dto.stepOrder = progressStep.getStepOrder();
            dto.status = progressStep.getStatus().name();
            dto.startAt = DateTimeUtils.format(progressStep.getStartAt());
            dto.closeAt = DateTimeUtils.format(progressStep.getCloseAt());
            dto.deadlineAt = DateTimeUtils.format(progressStep.getDeadlineAt());
            dto.projectId = progressStep.getProjectId();

            // 관련 결재가 존재하면 결재 ID를 설정
            if (progressStep.getRelatedApproval() != null) {
                dto.relatedApprovalId = progressStep.getRelatedApproval().getId();
            }

            // 결재가 결정되었으면 결재자 정보를 설정
            if (progressStep.isDecided()) {
                dto.approver = MemberResponseDto.MeResponseDto.fromEntity(
                    progressStep.getRelatedApproval().getApprover());
            }

            return dto;
        }
    }

}
