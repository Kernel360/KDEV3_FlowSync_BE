package com.checkping.infra.repository.approval;

import com.checkping.domain.approval.ApprovalCompleteHistory;
import com.checkping.info.approval.ApprovalCompleteHistorySearchInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApprovalCompleteHistoryReaderImpl implements ApprovalCompleteHistoryReader {

    private final ApprovalCompleteHistoryRepository approvalCompleteHistoryRepository;

    public Page<ApprovalCompleteHistory> search(Long projectId, ApprovalCompleteHistorySearchInfo condition) {

        // 페이지 객체 생성
        Pageable pageable = PageRequest.of(condition.getCurrentPage(), condition.getPageSize());

        return approvalCompleteHistoryRepository.search(projectId, condition, pageable);
    }

}
