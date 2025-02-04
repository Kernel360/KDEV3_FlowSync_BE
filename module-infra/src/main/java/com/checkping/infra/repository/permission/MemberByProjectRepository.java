package com.checkping.infra.repository.permission;

import com.checkping.domain.permission.MemberByProject;
import com.checkping.domain.permission.MemberByProjectId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberByProjectRepository extends JpaRepository<MemberByProject, MemberByProjectId> {

    boolean existsById(MemberByProjectId id);

}
