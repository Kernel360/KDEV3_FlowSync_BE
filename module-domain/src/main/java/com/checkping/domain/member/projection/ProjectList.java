package com.checkping.domain.member.projection;

import com.checkping.domain.project.Project;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProjectList {
    /*
   name : 프로젝트 이름
   developerName : 개발사
   customerName : 고객사
   managementStep : 프로젝트 관리 단계 * CONTRACT(계약), IN_PROGRESS(진행중), COMPLETED(납품완료), MAINTENANCE(하자보수), PAUSED(일시중단), DELETED(삭제)
   startAt : 시작일
   deadlineAt : 예정 마감일
   closeAt : 납품 완료일
   updateAt : 수정일
   */
    private final Long id;
    private final String name;
    private final String customerName;
    private final String developerName;
    private final Project.ManagementStep managementStep;
    private final LocalDateTime startAt;
    private final LocalDateTime deadlineAt;
    private final LocalDateTime closeAt;
    private final LocalDateTime updateAt;

    @QueryProjection
    public ProjectList(Long id, String name, String developerName, String customerName, Project.ManagementStep managementStep, LocalDateTime startAt, LocalDateTime closeAt, LocalDateTime updateAt, LocalDateTime deadlineAt) {
        this.id = id;
        this.name = name;
        this.developerName = developerName;
        this.customerName = customerName;
        this.managementStep = managementStep;
        this.startAt = startAt;
        this.deadlineAt = deadlineAt;
        this.closeAt = closeAt;
        this.updateAt = updateAt;
    }
}