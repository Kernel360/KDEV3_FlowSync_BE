package com.checkping.infra.repository.approval.file;

import com.checkping.domain.approval.ApprovalFile;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApprovalFileStoreImpl implements ApprovalFileStore {

    private final ApprovalFileRepository approvalFileRepository;

    /**
     * ApprovalFile 저장
     * @param approvalFile  ApprovalFile
     * @return  ApprovalFile
     */
    @Override
    public ApprovalFile store(ApprovalFile approvalFile) {
        return approvalFileRepository.save(approvalFile);
    }

    /**
     * ApprovalFile List 저장
     *
     * @param approvalFiles ApprovalFile List
     * @return  ApprovalFile List
     */
    @Override
    public List<ApprovalFile> store(List<ApprovalFile> approvalFiles) {
        return approvalFiles.stream().map(approvalFileRepository::save).toList();
    }
}
