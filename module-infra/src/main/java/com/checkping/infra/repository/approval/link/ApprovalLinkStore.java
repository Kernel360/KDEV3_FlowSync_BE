package com.checkping.infra.repository.approval.link;

import com.checkping.domain.approval.ApprovalLink;
import java.util.List;

public interface ApprovalLinkStore {

    ApprovalLink store(ApprovalLink approvalLink);

    List<ApprovalLink> store(List<ApprovalLink> approvalLinks);
}
