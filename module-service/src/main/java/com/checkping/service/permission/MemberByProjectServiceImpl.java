package com.checkping.service.permission;

import com.checkping.domain.permission.MemberByProjectId;
import com.checkping.infra.repository.permission.MemberByProjectRepository;
import com.checkping.infra.repository.project.ProjectReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemberByProjectServiceImpl implements MemberByProjectService{

    private final MemberByProjectRepository memberByProjectRepository;

    private final ProjectReader projectReader;

    @Override
    public boolean existsByMemberIdAndProjectId(Long memberId, Long projectId) {
        return memberByProjectRepository.existsById(new MemberByProjectId(memberId, projectId));
    }

    @Override
    public boolean isMemberApprovalForProject(Long memberId, Long projectId) {
        Long customerOwnerId = projectReader.getById(projectId).getCustomerOwner().getId();
        return Objects.equals(memberId, customerOwnerId);
    }
}