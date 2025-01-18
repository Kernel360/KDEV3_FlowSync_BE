package com.checkping.dto;

import com.checkping.domain.member.Member;
import com.checkping.domain.member.Organization;
import com.checkping.domain.project.Project;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

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
        id : 프로젝트 아이디
        name : 프로젝트 이름
        description : 프로젝트 설명
        detail : 프로젝트 세부 설명
        status : 프로젝트 상태 * IN_PROGRESS(진행중), PAUSED(일시 중단), COMPLETED(완료)
        management_step : 프로젝트 관리 단계 * CONTRACT(계약), IN_PROGRESS(진행중), COMPLETED(납품완료), MAINTENANCE(유지보수)
        startAt : 프로젝트 시작 일시
        closeAt : 프로젝트 종료 일시
        resisterId : 등록자 아이디
        deletedYn : 삭제여부
        */

        private String name;
        @Schema(description = "프로젝트 짧은 설명")
        private String description;
        @Schema(description = "프로젝트 긴 설명")
        private String detail;
        private String status;
        private String managementStep;
        @Schema(description = "프로젝트 시작 일시", example = "yyyy-MM-dd HH:mm:ss" )
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime startAt;
        @Schema(description = "프로젝트 마감 일시", examples = {"2025-01-15 10:17:15", "2025-12-28 11:17:15"} )
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime closeAt;
        private Long resisterId;

        private String developerOrgId;
        private String customerOrgId;

        private List<String> members;

        public static Project toEntity(ResisterDto resisterDto, List<Organization> organizations, List<Member> members) {
            return Project.builder()
                .name(resisterDto.getName())
                .description(resisterDto.getDescription())
                .detail(resisterDto.getDetail())
                .status(Project.Status.IN_PROGRESS)
                .management_step(Project.ManagementStep.IN_PROGRESS)
                .startAt(resisterDto.getStartAt())
                .closeAt(resisterDto.getCloseAt())
                .resisterId(resisterDto.getResisterId())
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
        status : 프로젝트 상태 * IN_PROGRESS(진행중), PAUSED(일시 중단), COMPLETED(완료)
        management_step : 프로젝트 관리 단계 * CONTRACT(계약), IN_PROGRESS(진행중), COMPLETED(납품완료), MAINTENANCE(유지보수)
        startAt : 프로젝트 시작 일시
        closeAt : 프로젝트 종료 일시
        updaterid : 수정자 ID
         */
        private String name;
        private String description;
        private String detail;
        private String status;
        private String managementStep;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime startAt;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime closeAt;
        private Long updaterId;

        public static Project toEntity(UpdateDto updateDto, Project existingProject) {
            return existingProject.toBuilder()
                .id(existingProject.getId())
                .name(updateDto.getName())
                .description(updateDto.getDescription())
                .detail(updateDto.getDetail())
                .status(Project.Status.valueOf(updateDto.getStatus()))
                .management_step(Project.ManagementStep.valueOf(updateDto.getManagementStep()))
                .startAt(updateDto.getStartAt())
                .closeAt(updateDto.getCloseAt())
                .updateAt(LocalDateTime.now())
                .updaterId(updateDto.getUpdaterId())
                .build();
        }
    }
}