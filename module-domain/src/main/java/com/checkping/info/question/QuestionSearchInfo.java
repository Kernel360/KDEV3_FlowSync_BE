package com.checkping.info.question;

import com.checkping.domain.question.Question.Category;
import com.checkping.domain.question.Question.Status;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionSearchInfo {

    /*
    category : 게시글 카테고리 (Enum 으로 변경해야함)
    status : 게시글 상태 (Enum 으로 변경해야함)
    keyword : 게시글 검색어 (String)
    currentPage: 현재 페이지
    pageSize: 페이지 사이즈
     */
    public record SearchCondition(Category category, Status status, String keyword,
                                  Integer currentPage, Integer pageSize) {

        public SearchCondition(Category category, Status status, String keyword,
            Integer currentPage, Integer pageSize) {
            this.category = category;
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
