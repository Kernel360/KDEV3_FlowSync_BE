package com.checkping.infra.repository.approval;

import com.checkping.info.approval.ApprovalCountProjection;
import java.util.List;

public interface ApprovalCustomRepository {

    List<ApprovalCountProjection> countByProgressStep(Long projectId);
}
