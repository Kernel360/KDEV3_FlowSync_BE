package com.checkping.service.project.progressstep;

import com.checkping.domain.member.Member;
import com.checkping.domain.member.Organization;
import com.checkping.domain.project.ProgressStep;
import com.checkping.domain.project.Project;
import com.checkping.dto.project.ProgressStepGet;
import com.checkping.dto.project.ProgressStepPlanUpdate.Request;
import com.checkping.dto.project.ProgressStepPlanUpdate.Response;
import com.checkping.exception.project.progressstep.ProgressStepMismatchProjectException;
import com.checkping.infra.repository.project.ProgressStepReader;
import com.checkping.infra.repository.project.ProjectReader;
import com.checkping.service.member.util.CurrentMemberUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProgressStepServiceImpl implements ProgressStepService {

    private final ProjectReader projectReader;
    private final ProgressStepReader progressStepReader;
    private final CurrentMemberUtil currentMemberUtil;

    @Transactional(readOnly = true)
    @Override
    public List<ProgressStepGet.Response> getProgressStep(Long projectId) {

        // check current member
        Member currentMember = currentMemberUtil.getCurrentMember();

        // find Project
        Project project = projectReader.getById(projectId);

        // check member organization
        checkOrganization(project, currentMember);

        // find progress steps
        List<ProgressStep> progressSteps = progressStepReader.getByProjectId(projectId);

        // Entity -> Dto
        return ProgressStepGet.Response.toDto(progressSteps);
    }

    @Transactional(readOnly = true)
    @Override
    public Response updateProgressStepPlan(Long projectId, Long progressStepId, Request request) {
        return null;
    }

    /**
     * check member organization
     *
     * @param project       project
     * @param currentMember current member
     * @throws ProgressStepMismatchProjectException progress step mismatch project exception
     */
    private void checkOrganization(Project project, Member currentMember) {

        // organization ids (organization -> organization id)
        List<Long> organizationIds = project.getOrganizations().stream()
            .map(Organization::getId).toList();

        // check member organization
        if (!organizationIds.contains(currentMember.getOrganization().getId())) {
            throw new ProgressStepMismatchProjectException();
        }
    }
}
