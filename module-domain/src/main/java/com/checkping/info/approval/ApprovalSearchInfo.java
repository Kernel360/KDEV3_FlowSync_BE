package com.checkping.info.approval;

import com.checkping.domain.approval.Approval.ApprovalStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApprovalSearchInfo {

    /*
    progressId : 프로젝트 진행 단계 ID
    status : 결재 상태 (Enum 으로 변경해야함)
    keyword : 검색어 (String)
    currentPage: 현재 페이지
    pageSize: 페이지 사이즈
     */

    public record SearchCondition(Long progressId, ApprovalStatus status, String keyword,
                                  Integer currentPage, Integer pageSize) {

        public SearchCondition(Long progressId, ApprovalStatus status, String keyword,
            Integer currentPage, Integer pageSize) {
            this.progressId = progressId;
            this.status = status;
            this.keyword = keyword;

            // currentPage 와 pageSize 가 null 이거나 0 이하일 경우 기본값으로 1, 10 으로 설정
            if (currentPage == null || currentPage < 0) {
                currentPage = 1;
            }
            this.currentPage = currentPage - 1;

            if (pageSize == null || pageSize < 0) {
                pageSize = 10;
            }
            this.pageSize = pageSize;
        }
    }
}
