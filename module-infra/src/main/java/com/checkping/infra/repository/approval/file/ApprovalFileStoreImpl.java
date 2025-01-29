package com.checkping.infra.repository.approval.file;

import com.checkping.domain.approval.ApprovalFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApprovalFileStoreImpl implements ApprovalFileStore {

    private final ApprovalFileRepository approvalFileRepository;

    @Override
    public ApprovalFile store(ApprovalFile approvalFile) {
        return approvalFileRepository.save(approvalFile);
    }
}
