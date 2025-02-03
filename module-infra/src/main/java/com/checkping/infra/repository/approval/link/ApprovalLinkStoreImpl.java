package com.checkping.infra.repository.approval.link;

import com.checkping.domain.approval.ApprovalLink;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApprovalLinkStoreImpl implements ApprovalLinkStore {

    private final ApprovalLinkRepository approvalLinkRepository;

    @Override
    public ApprovalLink store(ApprovalLink approvalLink) {
        return approvalLinkRepository.save(approvalLink);
    }

    @Override
    public List<ApprovalLink> store(List<ApprovalLink> approvalLinks) {
        return approvalLinkRepository.saveAll(approvalLinks);
    }

}
