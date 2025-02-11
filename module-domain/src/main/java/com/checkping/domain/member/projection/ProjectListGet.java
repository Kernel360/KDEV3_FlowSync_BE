package com.checkping.domain.member.projection;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProjectListGet {
    /*
   name : 프로젝트 이름
   developerName : 개발사
   customerName : 고객사
   managementStep : 프로젝트 관리 단계 * CONTRACT(계약), IN_PROGRESS(진행중), COMPLETED(납품완료), MAINTENANCE(하자보수), PAUSED(일시중단), DELETED(삭제)
   startAt : 프로젝트 시작 일시
   closeAt : 프로젝트 종료 일시
   updateAt : 프로젝트 수정 일시
   */
    private Long id;
    private String name;
    private String customerName;
    private String developerName;
    private LocalDateTime startAt;
    private LocalDateTime closeAt;
    private LocalDateTime updateAt;

    @QueryProjection
    public ProjectListGet(Long id, String name, String developerName, String customerName, LocalDateTime startAt, LocalDateTime closeAt, LocalDateTime updateAt) {
        this.id = id;
        this.name = name;
        this.developerName = developerName;
        this.customerName = customerName;
        this.startAt = startAt;
        this.closeAt = closeAt;
        this.updateAt = updateAt;

    }


}
