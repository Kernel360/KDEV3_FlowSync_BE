package com.checkping.service.approval;

import com.checkping.dto.approval.ApprovalRegister;

public interface ApprovalService {

    ApprovalRegister.Response register(Long projectId, ApprovalRegister.Request request);
}
