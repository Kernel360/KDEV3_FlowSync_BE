package com.checkping.service.approval;

import com.checkping.dto.approval.ApprovalRegister;
import com.checkping.dto.approval.ApprovalSearch;
import com.checkping.dto.approval.ApprovalSearchCondition;

public interface ApprovalService {

    ApprovalRegister.Response register(Long projectId, ApprovalRegister.Request request);

    ApprovalSearch.Response search(Long projectId, ApprovalSearchCondition request);
}
