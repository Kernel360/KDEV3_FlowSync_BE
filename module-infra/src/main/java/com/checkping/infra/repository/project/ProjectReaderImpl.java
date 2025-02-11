package com.checkping.infra.repository.project;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import com.checkping.domain.project.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectReaderImpl implements ProjectReader {

    private final ProjectRepository projectRepository;

    /**
     * 프로젝트 ID로 프로젝트 조회
     *
     * @param projectId 프로젝트 ID
     * @return 프로젝트
     */
    @Override
    public Project getById(Long projectId) {
        return projectRepository.findById(projectId)
            .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));
    }

    /**
     * 프로젝트 ID와 고객 ID로 고객이 프로젝트 소유자인지 확인
     *
     * @param projectId  프로젝트 ID
     * @param customerId 고객 ID
     * @return 고객이 프로젝트 소유자인지 여부
     */
    @Override
    public boolean isCustomerOwner(Long projectId, Long customerId) {
        return projectRepository.existsByIdAndCustomerOwnerId(projectId, customerId);
    }

    /**
     * 프로젝트 ID와 멤버 ID로 멤버가 개발자 최고 담당자인지 확인
     *
     * @param projectId 프로젝트 ID
     * @param memberId  멤버 ID
     * @return 멤버가 개발자 최고 담당자인지 여부
     */
    @Override
    public boolean isDevOwner(Long projectId, Long memberId) {
        return projectRepository.existsByIdAndDevOwnerId(projectId, memberId);
    }

    /**
     * 프로젝트 ID와 조직 ID가 일치하는지 확인
     *
     * @param projectId      프로젝트 ID
     * @param organizationId 조직 ID
     * @return 프로젝트 ID와 조직 ID가 일치하는지 여부
     */
    @Override
    public boolean matchProjectAndOrganization(Long projectId, Long organizationId) {
        return projectRepository.matchProjectAndOrganization(projectId, organizationId);
    }
}
