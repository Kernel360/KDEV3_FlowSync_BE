package com.checkping.dto.question;

import com.checkping.domain.question.Question.Category;
import com.checkping.domain.question.Question.Status;
import com.checkping.exception.question.QuestionCategoryException;
import com.checkping.exception.question.QuestionStatusException;
import com.checkping.info.question.QuestionSearchInfo;
import lombok.Getter;

@Getter
public class QuestionSearchCondition {

    /*
    category : 게시글 카테고리 (Enum 으로 변경해야함)
    status : 게시글 상태 (Enum 으로 변경해야함)
    keyword : 게시글 검색어 (String)
    currentPage: 현재 페이지
    pageSize: 페이지 사이즈
     */
    private String category;
    private String status;
    private String keyword;
    private Integer currentPage;
    private Integer pageSize;

    public static QuestionSearchInfo.SearchCondition toInfo(
        QuestionSearchCondition searchCondition) {
        return new QuestionSearchInfo.SearchCondition(
            convertCategory(searchCondition.getCategory()),
            convertStatus(searchCondition.getStatus()), searchCondition.getKeyword(),
            searchCondition.getCurrentPage(), searchCondition.getPageSize());
    }

    public QuestionSearchCondition(String category, String status, String keyword,
        Integer currentPage,
        Integer pageSize) {
        this.category = category;
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
