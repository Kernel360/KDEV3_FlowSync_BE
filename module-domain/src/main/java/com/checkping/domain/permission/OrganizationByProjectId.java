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
public class OrganizationByProjectId implements Serializable {
    /*
    orgId : 업체ID
    projectId : 프로젝트ID
   */
    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "project_id")
    private Long projectId;
}