package com.checkping.service.permission;

import com.checkping.domain.permission.MemberByProjectId;
import com.checkping.infra.repository.permission.MemberByProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberByProjectServiceImpl implements MemberByProjectService{

    private final MemberByProjectRepository memberByProjectRepository;

    @Override
    public boolean existsByMemberIdAndProjectId(Long memberId, Long projectId) {
        return memberByProjectRepository.existsById(new MemberByProjectId(memberId, projectId));
    }
}