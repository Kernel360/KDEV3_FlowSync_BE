package com.checkping.dto.project;

import com.checkping.domain.member.Member;
import com.checkping.domain.member.Organization;
import com.checkping.domain.project.Project;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProjectRequest {

    @Getter
    @ToString
    @Builder
    @Schema(description = "프로젝트 등록 DTO")
    public static class ResisterDto {

        /*
        name : 프로젝트 이름
        description : 프로젝트 설명
        detail : 프로젝트 세부 설명
        managementStep : 프로젝트 관리 단계 * CONTRACT(계약), IN_PROGRESS(진행중), COMPLETED(납품완료), MAINTENANCE(하자보수), PAUSED(일시중단)
        startAt : 프로젝트 시작 일시
        closeAt : 프로젝트 종료 일시
        resisterId : 등록자 아이디
        devOwnerId : 개발사 대표자 아이디
        customerOwnerId : 고객사 결재자 아이디
        developerOrgId : 개발사 아이디
        customerOrgId :고객사 아이디
        members : 추가할 멤버 목록
        */
        @Schema(description = "프로젝트 이름", example = "FlowSync")
        private String name;
        @Schema(description = "프로젝트 짧은 설명")
        private String description;
        @Schema(description = "프로젝트 긴 설명")
        private String detail;
        @Schema(description = "프로젝트 관리단계", example = "IN_PROGRESS")
        private String managementStep;
        @Schema(description = "프로젝트 시작 일시", example = "2025-01-15 10:17:15", type = "string")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime startAt;
        @Schema(description = "프로젝트 마감 일시", example = "2025-12-28 11:17:15", type = "string")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime closeAt;
        @Schema(description = "개발사 대표자 아이디", example = "1")
        private Long devOwnerId;
        @Schema(description = "고객사 결재자 아이디", example = "1")
        private Long customerOwnerId;
        @Schema(description = "개발사 아이디", example = "1")
        private Long developerOrgId;
        @Schema(description = "고객사 아이디", example = "2")
        private Long customerOrgId;
        @Schema(description = "추가할 멤버 아이디 목록")
        private List<Long> members;

        public static Project toEntity(ResisterDto resisterDto, List<Organization> organizations, List<Member> members) {
            Member devOwnerMember = Member.builder()
                    .id(resisterDto.getDevOwnerId())
                    .build();
            Member customerOwnerMember = Member.builder()
                    .id(resisterDto.getCustomerOwnerId())
                    .build();

            return Project.builder()
                .name(resisterDto.getName())
                .description(resisterDto.getDescription())
                .detail(resisterDto.getDetail())
                .managementStep(Project.ManagementStep.valueOf(resisterDto.getManagementStep()))
                .regAt(LocalDateTime.now())
                .startAt(resisterDto.getStartAt())
                .closeAt(resisterDto.getCloseAt())
                .devOwner(devOwnerMember)
                .customerOwner(customerOwnerMember)
                .organizations(organizations)
                .members(members)
                .deletedYn("N")
                .build();
        }
    }

    @Getter
    @ToString
    @Builder
    public static class UpdateDto {
        /*
       name : 프로젝트 이름
       description : 프로젝트 설명
       detail : 프로젝트 세부 설명
       managementStep : 프로젝트 관리 단계 * CONTRACT(계약), IN_PROGRESS(진행중), COMPLETED(납품완료), MAINTENANCE(하자보수), PAUSED(일시중단)
       startAt : 프로젝트 시작 일시
       closeAt : 프로젝트 종료 일시
       devOwnerId : 개발사 대표자 아이디
       developerOrgId : 개발사 아이디
       customerOwnerId : 고객사 결재자 아이디
       customerOrgId :고객사 아이디
       members : 추가할 멤버 목록
       */
        @Schema(description = "프로젝트 이름", example = "FlowSync")
        private String name;
        @Schema(description = "프로젝트 짧은 설명")
        private String description;
        @Schema(description = "프로젝트 긴 설명")
        private String detail;
        @Schema(description = "프로젝트 관리단계", example = "COMPLETED")
        private String managementStep;
        @Schema(description = "프로젝트 시작 일시", example = "2025-01-15 10:17:15", type = "string")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime startAt;
        @Schema(description = "프로젝트 마감 일시", examples = "2025-12-28 11:17:15", type = "string")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime closeAt;
        @Schema(description = "개발사 대표자 아이디", example = "1")
        private Long devOwnerId;
        @Schema(description = "고객사 결재자 아이디", example = "1")
        private Long customerOwnerId;
        @Schema(description = "개발사 아이디", example = "1")
        private Long developerOrgId;
        @Schema(description = "고객사 아이디", example = "2")
        private Long customerOrgId;
        @Schema(description = "추가할 멤버 아이디 목록")
        private List<Long> members;

        public static Project toEntity(UpdateDto updateDto, Project existingProject, List<Organization> organizations, List<Member> members) {
            Member devOwnerMember = Member.builder()
                    .id(updateDto.getDevOwnerId())
                    .build();
            Member customerOwnerMember = Member.builder()
                    .id(updateDto.getCustomerOwnerId())
                    .build();

            return existingProject.toBuilder()
                .id(existingProject.getId())
                .name(updateDto.getName())
                .description(updateDto.getDescription())
                .detail(updateDto.getDetail())
                .managementStep(Project.ManagementStep.valueOf(updateDto.getManagementStep()))
                .devOwner(devOwnerMember)
                .customerOwner(customerOwnerMember)
                .startAt(updateDto.getStartAt())
                .closeAt(updateDto.getCloseAt())
                .updateAt(LocalDateTime.now())
                .organizations(organizations)
                .members(members)
                .build();
        }
    }
}