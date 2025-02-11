package com.checkping.domain.project.projection;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class OwnerInfo {
    private String ownerOrgName;
    private String ownerName;
    private String profileImageUrl;
    private String jobRole;
    private String jobTitle;
    private String phoneNum;

    @QueryProjection
    public OwnerInfo(String ownerOrgName, String ownerName, String profileImageUrl, String jobRole, String jobTitle, String phoneNum) {
        this.ownerOrgName = ownerOrgName;
        this.ownerName = ownerName;
        this.profileImageUrl = profileImageUrl;
        this.jobRole = jobRole;
        this.jobTitle = jobTitle;
        this.phoneNum = phoneNum;
    }
}
