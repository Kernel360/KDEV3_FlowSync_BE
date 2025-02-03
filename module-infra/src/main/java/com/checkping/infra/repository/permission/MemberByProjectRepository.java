package com.checkping.infra.repository.permission;

import com.checkping.domain.permission.MemberByProject;
import com.checkping.domain.permission.MemberByProjectId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberByProjectRepository extends JpaRepository<MemberByProject, MemberByProjectId> {

    Optional<MemberByProject> findByIdMemberIdAndIdProjectId(Long memberId, Long projectId);
}
