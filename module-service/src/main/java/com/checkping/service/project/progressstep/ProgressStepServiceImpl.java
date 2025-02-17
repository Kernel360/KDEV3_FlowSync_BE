package com.checkping.service.project.progressstep;

import com.checkping.domain.member.Member;
import com.checkping.domain.project.ProgressStep;
import com.checkping.dto.project.ProgressStepGet.Response;
import com.checkping.dto.project.ProgressStepPlanUpdate;
import com.checkping.dto.project.ProgressStepPlanUpdate.Request;
import com.checkping.dto.project.ProgressStepRegister;
import com.checkping.exception.project.progressstep.ProgressStepMismatchProjectException;
import com.checkping.exception.project.progressstep.ProgressStepNotAfterStartAtException;
import com.checkping.exception.project.progressstep.ProgressStepNotFoundException;
import com.checkping.exception.project.progressstep.ProgressStepStartAtException;
import com.checkping.infra.repository.project.ProgressStepReader;
import com.checkping.infra.repository.project.ProgressStepStore;
import com.checkping.infra.repository.project.ProjectReader;
import com.checkping.service.member.util.CurrentMemberUtil;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProgressStepServiceImpl implements ProgressStepService {

    private final ProjectReader projectReader;
    private final ProgressStepReader progressStepReader;
    private final ProgressStepStore progressStepStore;
    private final CurrentMemberUtil currentMemberUtil;

    @Transactional(readOnly = true)
    @Override
    public List<Response> getProgressStep(Long projectId) {

        // check current member
        Member currentMember = currentMemberUtil.getCurrentMember();

        // check member organization
        checkOrganization(projectId, currentMember);

        // find progress steps
        List<ProgressStep> progressSteps = progressStepReader.getByProjectId(projectId);

        // Entity -> Dto
        return Response.toDto(progressSteps);
    }

    @Transactional
    @Override
    public ProgressStepPlanUpdate.Response updateProgressStepPlan(Long projectId,
        Long progressStepId, Request request) {
        // TODO : 권한 처리를 인터셉터에서 하도록 하며, 개발사 오너 담당자만 수정 가능 처리해야 한다.

        // 프로젝트 진행단계 시작일시 보다 마감일시가 이전이면 예외 발생
        LocalDateTime startAt = request.getStartAt();
        LocalDateTime deadlineAt = request.getDeadlineAt();
        boolean hasStartAt = Objects.nonNull(startAt);
        boolean hasDeadlineAt = Objects.nonNull(deadlineAt);

        // 경우의 수
        // 기입한 데이터가 없는 경우
        // 1. (null, null)
        // 1-1. 초기값 : (null, null) / 변경값 : (null, null) -> 예외 발생
        if (!hasStartAt && !hasDeadlineAt) {
            throw new ProgressStepStartAtException();
        }

        // 유효성 검사를 하기 위한 ProgressStep 조회
        // Find ProgressStep by Id and ProjectId - 프로젝트에 속한 단계인지 확인과 동시에 단계 정보를 가져옴
        ProgressStep progressStep = progressStepReader.getByIdAndProjectId(progressStepId,
            projectId).orElseThrow(ProgressStepNotFoundException::new);

        LocalDateTime newStartAt = null;
        LocalDateTime newDeadlineAt = null;

        // 1. 시작 일시만 기입한 경우
        // 1-1. 초기값 : (null, null) / 변경값 : (시작일시 값, null) -> 시작일시만 기입
        // 1-2. 초기값 : (시작일시 값, null) / 변경값 : (시작일시 값, null) -> 시작일시만 변경
        // 1-3. 초기값 : (null, 마감일시 값) / 변경값 : (시작일시 값, null) -> 시작일시 변경, 마감일시는 그대로
        // 1-4. 초기값 : (시작일시 값, 마감일시 값) / 변경값 : (시작일시 값, null) -> 시작일시만 변경
        if (hasStartAt) {
            newStartAt = startAt;
        }

        // 2. 마감 일시만 기입한 경우
        // 2-1. 초기값 : (null, null) / 변경값 : (null, 마감일시) -> 마감일시만 기입
        // 2-2. 초기값 : (시작일시 값, null) / 변경값 : (null, 마감일시 값) -> 마감일시 변경, 시작일시는 그대로
        // 2-3. 초기값 : (null, 마감일시 값) / 변경값 : (null, 마감일시 값) -> 마감일시만 변경
        // 2-4. 초기값 : (시작일시 값, 마감일시 값) / 변경값 : (null, 마감일시 값) -> 마감일시 변경, 시작일시는 그대로
        if (hasDeadlineAt) {
            newDeadlineAt = deadlineAt;
        }

        // 3. 시작 일시와 마감 일시 모두 기입한 경우 (생략)
        // 4. 시작 일시보다 마감 일시가 더 늦은 경우
        if (hasStartAt && hasDeadlineAt && newStartAt.isAfter(newDeadlineAt)) {
            throw new ProgressStepNotAfterStartAtException();
        }

        // Update ProgressStep Plan
        progressStep.updatePlan(newStartAt, newDeadlineAt);

        // Entity -> Dto
        return ProgressStepPlanUpdate.Response.toDto(progressStep);
    }

    @Override
    @Transactional
    public ProgressStepRegister.Response registerProgressStep(Long projectId,
        ProgressStepRegister.Request request) {

        // check current member
        Member member = currentMemberUtil.getCurrentMember();

        // check member organization
        checkOrganization(projectId, member);

        // Max Step Order
        Integer maxStepOrder = progressStepReader.getByProjectId(projectId).stream()
            .map(ProgressStep::getStepOrder).max(Integer::compareTo).orElse(0);

        // register progress step
        ProgressStep init = ProgressStepRegister.Request.toEntity(request, projectId,
            maxStepOrder + 1);

        // Save ProgressStep
        ProgressStep progressStep = progressStepStore.store(init);

        // Entity -> Dto
        return ProgressStepRegister.Response.toDto(progressStep);
    }

    /**
     * check member organization
     *
     * @param projectId     projectId
     * @param currentMember current member
     * @throws ProgressStepMismatchProjectException progress step mismatch project exception
     */
    private void checkOrganization(Long projectId, Member currentMember) {

        // check admin
        if (currentMember.isAdmin()) {
            return;
        }

        // check project member
        boolean isProjectMember = projectReader.matchProjectAndOrganization(projectId,
            currentMember.getOrganization().getId());

        if (!isProjectMember) {
            throw new ProgressStepMismatchProjectException();
        }
    }
}
