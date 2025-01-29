package com.checkping.service.approval;

import com.checkping.domain.approval.Approval;
import com.checkping.domain.approval.ApprovalFile;
import com.checkping.dto.approval.ApprovalRegister;
import com.checkping.dto.approval.file.ApprovalFileRegister;
import com.checkping.infra.repository.approval.ApprovalStore;
import com.checkping.infra.repository.approval.file.ApprovalFileStore;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApprovalServiceImpl implements ApprovalService {

    private final ApprovalStore approvalStore;
    private final ApprovalFileStore approvalFileStore;

    @Override
    @Transactional
    public ApprovalRegister.Response register(Long projectId, ApprovalRegister.Request request) {

        // TODO : registerId 는 시큐리티에서 가져오도록 변경 필요
        Long registerId = 123123L;

        Approval init = ApprovalRegister.Request.toEntity(projectId,
            registerId, request);

        Approval approval = approvalStore.store(init);

        // FileRequest -> Entity
        List<ApprovalFile> approvalFiles = ApprovalFileRegister.Request.toEntity(approval, request.getFileInfoList());
        // Save approvalFiles
        approvalFileStore.store(approvalFiles);
        // Add approvalFiles to approval
        approval.addFiles(approvalFiles);


        return ApprovalRegister.Response.toDto(approval);
    }
}
