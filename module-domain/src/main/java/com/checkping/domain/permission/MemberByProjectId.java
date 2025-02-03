package com.checkping.domain.permission;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class MemberByProjectId implements Serializable {
    /*
    memberId : 멤버ID
    projectId : 업체ID
   */
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "project_id")
    private Long projectId;
}