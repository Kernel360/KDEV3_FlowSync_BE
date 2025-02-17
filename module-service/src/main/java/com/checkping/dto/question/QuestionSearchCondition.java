package com.checkping.dto.question;

import com.checkping.domain.question.Question.Category;
import com.checkping.domain.question.Question.Status;
import com.checkping.exception.question.QuestionCategoryException;
import com.checkping.exception.question.QuestionStatusException;
import com.checkping.info.question.QuestionSearchInfo;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionSearchCondition {

    /*
    category : 게시글 카테고리 (Enum 으로 변경해야함)
    progressStepId : 게시글 진행 단계 ID
    status : 게시글 상태 (Enum 으로 변경해야함)
    keyword : 게시글 검색어 (String)
    currentPage: 현재 페이지
    pageSize: 페이지 사이즈
     */
    private Long progressStepId;
    private String status;
    private String keyword;
    @Min(1)
    private Integer currentPage = 1;
    @Min(5)
    private Integer pageSize = 10;

    public static QuestionSearchInfo.SearchCondition toInfo(
        QuestionSearchCondition searchCondition, boolean adminSearch) {
        return new QuestionSearchInfo.SearchCondition(searchCondition.getProgressStepId(),
            convertStatus(searchCondition.getStatus()), searchCondition.getKeyword(),
            searchCondition.getCurrentPage() - 1, searchCondition.getPageSize(), adminSearch);
    }

    public QuestionSearchCondition(Long progressStepId, String status, String keyword,
        Integer currentPage, Integer pageSize) {
        this.progressStepId = progressStepId;
        this.status = status;
        this.keyword = keyword;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    /**
     * Enum : Category 변환 함수
     *
     * @param value Category 로 변환할 문자열
     * @return Question.Category
     */
    public static Category convertCategory(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        try {
            return Category.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new QuestionCategoryException(value);
        }
    }

    /**
     * Enum : Status 변환 함수
     *
     * @param value Status 로 변환할 문자열
     * @return Question.Status
     */
    public static Status convertStatus(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        try {
            return Status.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new QuestionStatusException(value);
        }
    }
}
