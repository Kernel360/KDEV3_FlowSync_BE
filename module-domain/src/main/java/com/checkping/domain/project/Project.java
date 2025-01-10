package com.checkping.domain.project;

import com.checkping.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Getter
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name="project")
@Entity
public class Project extends BaseEntity {

    /*
    id : 프로젝트 아이디
    name : 프로젝트 이름
    description : 프로젝트 설명
    detail : 프로젝트 세부 설명
    status : 프로젝트 상태 * IN_PROGRESS(진행중), PAUSED(일시 중단), COMPLETED(완료), DELETED(삭제)
    reg_at : 프로젝트 등록 일시
    update_at : 프로젝트 수정 일시
    close_at : 프로젝트 종료 일시
    resister_id : 등록자 아이디
    updater_id : 수정자 아이디
    deleted_yn : 삭제여부
    */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 추후 UUID로 수정
    private Long id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "detail", length = 255)
    private String detail;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 100)
    private Status status;

    @Column(name = "reg_at")
    private LocalDateTime regAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Column(name = "close_at")
    private LocalDateTime closeAt;

    @Column(name = "resister_id")
    private Long resisterId;

    @Column(name = "updater_id")
    private Long updaterId;

    @Column(name = "deleted_yn")
    private String deletedYn;

    @Getter
    @RequiredArgsConstructor
    public enum Status {
        IN_PROGRESS("진행중"),
        PAUSED("일시 중단"),
        COMPLETED("완료"),
        DELETED("삭제");

        private final String description;
    }

}