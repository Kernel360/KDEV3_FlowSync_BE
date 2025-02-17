package com.checkping.dto.project;


import com.checkping.common.dto.PageMetaResponse;
import com.checkping.common.utils.DateTimeUtils;
import com.checkping.domain.project.Project;
import com.checkping.domain.project.projection.OwnerInfo;
import com.checkping.domain.project.projection.ProjectInfo;
import com.checkping.infra.dto.ProjectUpdateDetailsDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProjectResponse {

    @Getter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectDto {
        @Schema(description = "프로젝트 아이디")
        private Long id;
        @Schema(description = "프로젝트 이름")
        private String name;
        @Schema(description = "프로젝트 짧은 설명")
        private String description;
        @Schema(description = "프로젝트 긴 설명")
        private String detail;
        @Schema(description = "프로젝트 관리 단계")
        private Project.ManagementStep managementStep;
        @Schema(description = "프로젝트 등록 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private String regAt;
        @Schema(description = "프로젝트 수정 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private String updateAt;
        @Schema(description = "프로젝트 시작 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private String startAt;
        @Schema(description = "프로젝트 예상 종료 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private String deadlineAt;
        @Schema(description = "프로젝트 종료 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private String closeAt;
        @Schema(description = "프로젝트 삭제여부")
        private String deletedYn;
        @Schema(description = "개발사 대표자 아이디")
        private Long devOwnerId;
        @Schema(description = "고객사 결재자 아이디")
        private Long customerOwnerId;
        @Schema(description = "개발사 이름")
        private String developerName;
        @Schema(description = "고객사 이름")
        private String customerName;

        public static ProjectDto toDto(Project project) {
            return ProjectDto.builder()
                    .id(project.getId())
                    .name(project.getName())
                    .description(project.getDescription())
                    .detail(project.getDetail())
                    .managementStep(project.getManagementStep())
                    .regAt(DateTimeUtils.format(project.getRegAt()))
                    .updateAt(DateTimeUtils.format(project.getUpdateAt()))
                    .startAt(DateTimeUtils.format(project.getStartAt()))
                    .deadlineAt(DateTimeUtils.format(project.getDeadlineAt()))
                    .closeAt(DateTimeUtils.format(project.getCloseAt()))
                    .deletedYn(project.getDeletedYn())
                    .devOwnerId(project.getDevOwner().getId())
                    .customerOwnerId(project.getCustomerOwner().getId())
                    .developerName(project.getOrganizations().get(0).getName())
                    .customerName(project.getOrganizations().get(1).getName())
                    .build();
        }
    }

    @Getter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectInfoDto {
        @Schema(description = "프로젝트 아이디")
        private Long id;
        @Schema(description = "프로젝트 이름")
        private String projectName;
        @Schema(description = "프로젝트 짧은 설명")
        private String description;
        @Schema(description = "프로젝트 관리단계")
        private Project.ManagementStep managementStep;
        @Schema(description = "개발사 이름")
        private String developerOrgName;
        @Schema(description = "개발사 대표자 이름")
        private String developerOwnerName;
        @Schema(description = "개발사 대표자 프로필 이미지 url")
        private String developerProfileImageUrl;
        @Schema(description = "개발사 대표자 직무")
        private String developerJobRole;
        @Schema(description = "개발사 대표자 직급")
        private String developerJobTitle;
        @Schema(description = "개발사 대표자 연락처")
        private String developerPhoneNum;
        @Schema(description = "고객사 이름")
        private String customerOrgName;
        @Schema(description = "고객사 대표자 이름")
        private String customerOwnerName;
        @Schema(description = "고객사 대표자 프로필 이미지 url")
        private String customerProfileImageUrl;
        @Schema(description = "고객사 대표자 직무")
        private String customerJobRole;
        @Schema(description = "고객사 대표자 직급")
        private String customerJobTitle;
        @Schema(description = "고객사 대표자 연락처")
        private String customerPhoneNum;
        @Schema(description = "프로젝트 시작 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime startAt;
        @Schema(description = "프로젝트 예상 종료 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime deadlineAt;
        @Schema(description = "프로젝트 종료 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime closeAt;

        public static ProjectInfoDto toDto(ProjectInfo projectInfo, OwnerInfo developerOwnerInfo, OwnerInfo customerOwnerInfo) {
            return ProjectInfoDto.builder()
                    .id(projectInfo.getId())
                    .projectName(projectInfo.getProjectName())
                    .description(projectInfo.getDescription())
                    .managementStep(projectInfo.getManagementStep())
                    .developerOrgName(developerOwnerInfo != null ? developerOwnerInfo.getOwnerOrgName() : null)
                    .developerOwnerName(developerOwnerInfo != null ? developerOwnerInfo.getOwnerName() : null)
                    .developerProfileImageUrl(developerOwnerInfo != null ? developerOwnerInfo.getProfileImageUrl() : null)
                    .developerJobRole(developerOwnerInfo != null ? developerOwnerInfo.getJobRole() : null)
                    .developerJobTitle(developerOwnerInfo != null ? developerOwnerInfo.getJobTitle() : null)
                    .developerPhoneNum(developerOwnerInfo != null ? developerOwnerInfo.getPhoneNum() : null)
                    .customerOrgName(customerOwnerInfo != null ? customerOwnerInfo.getOwnerOrgName() : null)
                    .customerOwnerName(customerOwnerInfo != null ? customerOwnerInfo.getOwnerName() : null)
                    .customerProfileImageUrl(customerOwnerInfo != null ? customerOwnerInfo.getProfileImageUrl() : null)
                    .customerJobRole(customerOwnerInfo != null ? customerOwnerInfo.getJobRole() : null)
                    .customerJobTitle(customerOwnerInfo != null ? customerOwnerInfo.getJobTitle() : null)
                    .customerPhoneNum(customerOwnerInfo != null ? customerOwnerInfo.getPhoneNum() : null)
                    .startAt(projectInfo.getStartAt())
                    .deadlineAt(projectInfo.getDeadlineAt())
                    .closeAt(projectInfo.getCloseAt())
                    .build();
        }
    }

    @Getter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectListDetailDto {
        @Schema(description = "프로젝트 아이디")
        private Long id;
        @Schema(description = "프로젝트 이름")
        private String name;
        @Schema(description = "프로젝트 짧은 설명")
        private String description;
        @Schema(description = "프로젝트 긴 설명")
        private String detail;
        @Schema(description = "프로젝트 관리 단계")
        private Project.ManagementStep managementStep;
        @Schema(description = "프로젝트 등록 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private String regAt;
        @Schema(description = "프로젝트 수정 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private String updateAt;
        @Schema(description = "프로젝트 시작 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private String startAt;
        @Schema(description = "프로젝트 예상 종료 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private String deadlineAt;
        @Schema(description = "프로젝트 종료 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private String closeAt;
        @Schema(description = "프로젝트 삭제여부")
        private String deletedYn;
        @Schema(description = "개발사 대표자 아이디")
        private Long devOwnerId;
        @Schema(description = "개발사 이름")
        private String developerName;
        @Schema(description = "고객사 이름")
        private String customerName;
        @Schema(description = "프로젝트 클릭 가능 여부")
        private Integer clickable;

        public ProjectListDetailDto(long id, String name, String description, String detail, String managementStep, Date regAt, Date updateAt, Date startAt, Date deadlineAt, Date closeAt, String deletedYn, long devOwnerId, String developerName, String customerName, int clickable) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.detail = detail;
            this.managementStep = Project.ManagementStep.valueOf(managementStep);
            this.regAt = DateTimeUtils.format(regAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            this.updateAt = DateTimeUtils.format(updateAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            this.startAt = DateTimeUtils.format(startAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            this.deadlineAt = deadlineAt == null ? null
                    : DateTimeUtils.format(deadlineAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            this.closeAt = closeAt == null ? null
                    : DateTimeUtils.format(closeAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            this.deletedYn = deletedYn;
            this.devOwnerId = devOwnerId;
            this.developerName = developerName;
            this.customerName = customerName;
            this.clickable = clickable;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectListDto {
        private List<ProjectListDetailDto> projects;
        private Map<String, Object> meta;

        public static ProjectListDto fromEntityPage(Page<ProjectListDetailDto> page) {

            List<ProjectListDetailDto> projectDtos = page.getContent().stream()
                    .toList();

            PageMetaResponse meta = PageMetaResponse.fromPage(page);
            Map<String, Object> result = meta.toMap();

            return new ProjectListDto(projectDtos, result);
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectManagementStepCountDto {
        private Map<String, Long> managementStepCountMap;

        public static ProjectManagementStepCountDto toDto(Map<String, Long> managementStepCountMap) {
            return new ProjectManagementStepCountDto(managementStepCountMap);
        }
    }

    @Getter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectUpdateDto {
        @Schema(description = "프로젝트 아이디")
        private Long id;
        @Schema(description = "프로젝트 이름")
        private String name;
        @Schema(description = "프로젝트 짧은 설명")
        private String description;
        @Schema(description = "프로젝트 긴 설명")
        private String detail;
        @Schema(description = "프로젝트 관리 단계")
        private Project.ManagementStep managementStep;
        @Schema(description = "프로젝트 시작 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date startAt;
        @Schema(description = "프로젝트 예상 종료 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date deadlineAt;
        @Schema(description = "프로젝트 종료 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date closeAt;
        @Schema(description = "개발사 대표자 아이디")
        private Long devOwnerId;
        @Schema(description = "고객사 결재자 아이디")
        private Long customerOwnerId;
        @Schema(description = "개발사 아이디")
        private Long developerOrgId;
        @Schema(description = "고객사 아이디")
        private Long customerOrgId;
        @Schema(description = "프로젝트 멤버")
        private List<Long> members;

        public static ProjectUpdateDto toDto(ProjectUpdateDetailsDto detailsDto, List<Long> members) {
            return ProjectUpdateDto.builder()
                    .id(detailsDto.getId())
                    .name(detailsDto.getName())
                    .description(detailsDto.getDescription())
                    .detail(detailsDto.getDetail())
                    .managementStep(Project.ManagementStep.valueOf(detailsDto.getManagementStep()))
                    .startAt(detailsDto.getStartAt())
                    .deadlineAt(detailsDto.getDeadlineAt())
                    .closeAt(detailsDto.getCloseAt())
                    .devOwnerId(detailsDto.getDevOwnerId())
                    .customerOwnerId(detailsDto.getCustomerOwnerId())
                    .developerOrgId(detailsDto.getDeveloperOrgId())
                    .customerOrgId(detailsDto.getCustomerOrgId())
                    .members(members)
                    .build();
        }
    }

    @Getter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectByManagementStepDto {
        @Schema(description = "프로젝트 아이디")
        private Long id;
        @Schema(description = "프로젝트 이름")
        private String name;
        @Schema(description = "프로젝트 클릭 가능 여부")
        private Integer clickable;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectListByManagementStepDto {
        private List<ProjectByManagementStepDto> projects;
        private Map<String, Object> meta;

        public static ProjectListByManagementStepDto toDto(Page<ProjectByManagementStepDto> projects) {
            List<ProjectByManagementStepDto> projectDtos = projects.getContent().stream().toList();

            PageMetaResponse meta = PageMetaResponse.fromPage(projects);
            Map<String, Object> result = meta.toMap();

            return new ProjectListByManagementStepDto(projectDtos, result);
        }
    }
}