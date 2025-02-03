package com.checkping.domain.permission;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_by_project")
@Entity
public class MemberByProject {
    /*
    id : 멤버 아이디와 프로젝트 아이디
     */
    @EmbeddedId
    private MemberByProjectId id;  // 복합 키
}
