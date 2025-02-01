package com.checkping.infra.repository.approval.comment;

import com.checkping.domain.approval.ApprovalComment;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApprovalCommentReaderImpl implements ApprovalCommentReader {

    private final ApprovalCommentRepository approvalCommentRepository;

    @Override
    public Optional<ApprovalComment> getById(Long id) {
        return approvalCommentRepository.findById(id);
    }

    @Override
    public boolean isContainingComment(Long approvalId, Long commentId) {
        return approvalCommentRepository.existsByApprovalIdAndId(approvalId, commentId);
    }

}
