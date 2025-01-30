package com.checkping.dto.approval;

import com.checkping.domain.approval.Approval.ApprovalStatus;
import com.checkping.exception.approval.ApprovalStatusException;
import com.checkping.info.approval.ApprovalSearchInfo;
import lombok.Getter;

@Getter
public class ApprovalSearchCondition {

    /*
    progressId : 프로젝트 진행 단계 ID
    status : 결재 상태 (Enum 으로 변경해야함)
    keyword : 검색어 (String)
    currentPage: 현재 페이지
    pageSize: 페이지 사이즈
     */

    private Long progressId;
    private String status;
    private String keyword;
    private Integer currentPage;
    private Integer pageSize;

    public static ApprovalSearchInfo.SearchCondition toInfo(
        ApprovalSearchCondition searchCondition) {
        return new ApprovalSearchInfo.SearchCondition(
            searchCondition.getProgressId(),
            ApprovalSearchCondition.convertStatus(searchCondition.getStatus()),
            searchCondition.getKeyword(),
            searchCondition.getCurrentPage(), searchCondition.getPageSize());
    }

    public ApprovalSearchCondition(Long progressId, String status, String keyword,
        Integer currentPage,
        Integer pageSize) {
        this.progressId = progressId;
        this.status = status;
        this.keyword = keyword;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    /**
     * Enum : ApprovalStatus 변환 함수
     *
     * @param value ApprovalStatus 로 변환할 문자열
     * @return  ApprovalStatus
     */
    public static ApprovalStatus convertStatus(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        try {
            return ApprovalStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApprovalStatusException(value);
        }
    }
}
