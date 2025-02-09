package com.checkping.infra.repository.approval;

import com.checkping.domain.approval.Approval;
import com.checkping.info.approval.ApprovalSearchInfo;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApprovalReaderImpl implements ApprovalReader {

    private final ApprovalRepository approvalRepository;

    @Override
    public Optional<Approval> getById(Long approvalId) {
        return approvalRepository.findById(approvalId);
    }

    @Override
    public Optional<Approval> getByIdWithComments(Long approvalId) {
        return approvalRepository.getByIdWithComments(approvalId);
    }

    @Override
    public Page<Approval> getApprovals(Long projectId,
        ApprovalSearchInfo.SearchCondition searchCondition) {

        // 페이지 객체 생성
        Pageable pageable = PageRequest.of(searchCondition.currentPage(),
            searchCondition.pageSize());

        // keyword / status / progressStep 검색 조건 여부 확인
        boolean isKeyword = searchCondition.keyword() != null && !searchCondition.keyword()
            .isEmpty();
        boolean isStatus = searchCondition.status() != null;
        boolean isProgressStep = searchCondition.progressId() != null;

        // Search all - 검색 조건이 없는 경우
        if (!isKeyword && !isStatus && !isProgressStep) {
            return approvalRepository.findByProjectId(projectId, pageable);
        }

        // Search keyword - 검색어만 있는 경우
        if (isKeyword && !isStatus && !isProgressStep) {
            return approvalRepository.findByProjectIdAndTitleContaining(
                projectId, searchCondition.keyword(), pageable);
        }

        // Search status - 상태만 있는 경우
        if (!isKeyword && isStatus && !isProgressStep) {
            return approvalRepository.findByProjectIdAndStatus(
                projectId, searchCondition.status(), pageable);
        }

        // Search progressStep - 진행상태만 있는 경우
        if (!isKeyword && !isStatus && isProgressStep) {
            return approvalRepository.findByProjectIdAndProgressStepId(
                projectId, searchCondition.progressId(), pageable);
        }

        // Search keyword and status - 검색어와 상태가 있는 경우
        if (isKeyword && isStatus && !isProgressStep) {
            return approvalRepository.findByProjectIdAndTitleContainingAndStatus(
                projectId, searchCondition.keyword(), searchCondition.status(), pageable);
        }

        // Search keyword and progressStep - 검색어와 진행상태가 있는 경우
        if (isKeyword && !isStatus && isProgressStep) {
            return approvalRepository.findByProjectIdAndTitleContainingAndProgressStepId(
                projectId, searchCondition.keyword(), searchCondition.progressId(), pageable);
        }

        // Search status and progressStep - 상태와 진행상태가 있는 경우
        if (!isKeyword && isStatus && isProgressStep) {
            return approvalRepository.findByProjectIdAndProgressStepIdAndStatus(
                projectId, searchCondition.progressId(), searchCondition.status(), pageable);
        }

        // Search keyword and status and progressStep - 검색어와 상태와 진행상태가 있는 경우
        return approvalRepository.findByProjectIdAndTitleContainingAndProgressStepIdAndStatus(
            projectId, searchCondition.keyword(), searchCondition.progressId(),
            searchCondition.status(), pageable);
    }

    @Override
    public boolean isContainingApproval(Long projectId, Long approvalId) {
        return approvalRepository.existsByProjectIdAndId(projectId, approvalId);
    }

    @Override
    public Long countByProgressStep(Long projectId, Long progressStepId) {
        return approvalRepository.countByProgressStep(projectId, progressStepId);
    }
}
