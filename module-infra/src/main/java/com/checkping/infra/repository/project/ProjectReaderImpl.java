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
}
