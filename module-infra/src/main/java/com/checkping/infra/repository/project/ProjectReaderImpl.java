package com.checkping.infra.repository.project;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import com.checkping.domain.project.Project;
import com.fasterxml.jackson.databind.ser.Serializers.Base;
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
}
