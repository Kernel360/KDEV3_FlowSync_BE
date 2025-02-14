package com.checkping.info.approval;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApprovalCompleteHistorySearchInfo {
    /*
    progressId : 프로젝트 진행 단계 ID
    currentPage : 현재 페이지
    pageSize : 페이지 사이즈
     */
    private Long progressId;
    private Integer currentPage;
    private Integer pageSize;

    public ApprovalCompleteHistorySearchInfo(Long progressId, Integer currentPage,
        Integer pageSize) {
        this.progressId = progressId;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }
}
