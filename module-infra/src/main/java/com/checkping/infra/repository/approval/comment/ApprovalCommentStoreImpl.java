package com.checkping.infra.repository.approval.comment;

import com.checkping.domain.approval.ApprovalComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApprovalCommentStoreImpl implements ApprovalCommentStore {

    private final ApprovalCommentRepository approvalCommentRepository;

    @Override
    public ApprovalComment store(ApprovalComment approvalComment) {
        return approvalCommentRepository.save(approvalComment);
    }
}
