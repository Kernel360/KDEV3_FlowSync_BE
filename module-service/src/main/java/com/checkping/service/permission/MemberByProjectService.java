package com.checkping.service.permission;

public interface MemberByProjectService {

    boolean existsByMemberIdAndProjectId(Long memberId, Long projectId);
}
