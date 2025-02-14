package com.checkping.service.project.progressstep;

import com.checkping.domain.member.Member;
import com.checkping.domain.member.Organization;
import com.checkping.domain.project.ProgressStep;
import com.checkping.domain.project.Project;
import com.checkping.dto.project.ProgressStepGet;
import com.checkping.dto.project.ProgressStepPlanUpdate;
import com.checkping.exception.project.progressstep.ProgressStepMismatchProjectException;
import com.checkping.exception.project.progressstep.ProgressStepNotAfterStartAtException;
import com.checkping.exception.project.progressstep.ProgressStepNotFoundException;
import com.checkping.infra.repository.project.ProgressStepReader;
import com.checkping.infra.repository.project.ProjectReader;
import com.checkping.service.member.util.CurrentMemberUtil;
import java.time.LocalDateTime;
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

    @Transactional
    @Override
    public ProgressStepPlanUpdate.Response updateProgressStepPlan(Long projectId, Long progressStepId, ProgressStepPlanUpdate.Request request) {
        // TODO : 권한 처리를 인터셉터에서 하도록 하며, 개발사 오너 담당자만 수정 가능 처리해야 한다.

        // 프로젝트 진행단계 시작일시 보다 마감일시가 이전이면 예외 발생
        LocalDateTime startAt = request.getStartAt();
        LocalDateTime deadlineAt = request.getDeadlineAt();
        if (!startAt.isAfter(deadlineAt)) {
            throw new ProgressStepNotAfterStartAtException();
        }

        // Find ProgressStep by Id and ProjectId - 프로젝트에 속한 단계인지 확인과 동시에 단계 정보를 가져옴
        ProgressStep progressStep = progressStepReader.getByIdAndProjectId(progressStepId,
            projectId).orElseThrow(
            ProgressStepNotFoundException::new);

        // Update ProgressStep Plan
        progressStep.updatePlan(request.getStartAt(), request.getDeadlineAt());

        // Entity -> Dto
        return ProgressStepPlanUpdate.Response.toDto(progressStep);
    }

    /**
     * check member organization
     *
     * @param project       project
     * @param currentMember current member
     * @throws ProgressStepMismatchProjectException progress step mismatch project exception
     */
    private void checkOrganization(Project project, Member currentMember) {

        // check admin
        if(currentMember.isAdmin()) {
            return;
        }

        // organization ids (organization -> organization id)
        List<Long> organizationIds = project.getOrganizations().stream()
            .map(Organization::getId).toList();

        // check member organization
        if (!organizationIds.contains(currentMember.getOrganization().getId())) {
            throw new ProgressStepMismatchProjectException();
        }
    }
}
