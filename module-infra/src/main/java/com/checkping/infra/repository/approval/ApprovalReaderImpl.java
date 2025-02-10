package com.checkping.infra.repository.approval;

import com.checkping.domain.approval.Approval;
import com.checkping.info.approval.ApprovalCountProjection;
import com.checkping.info.approval.ApprovalSearchInfo;
import java.util.List;
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

        return approvalRepository.getByCondition(projectId, searchCondition, pageable);
    }

    @Override
    public boolean isContainingApproval(Long projectId, Long approvalId) {
        return approvalRepository.existsByProjectIdAndId(projectId, approvalId);
    }

    @Override
    public List<ApprovalCountProjection> countByProgressStep(Long projectId) {
        return approvalRepository.countByProgressStep(projectId);
    }
}
