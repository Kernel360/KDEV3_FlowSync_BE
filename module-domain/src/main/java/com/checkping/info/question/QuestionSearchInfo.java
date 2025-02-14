package com.checkping.info.question;

import com.checkping.domain.question.Question.Status;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionSearchInfo {

    /*
    progressId : 게시글 진행 단계 ID
    status : 게시글 상태 (Enum 으로 변경해야함)
    keyword : 게시글 검색어 (String)
    currentPage: 현재 페이지
    pageSize: 페이지 사이즈

     */
    public record SearchCondition(Long progressId, Status status, String keyword,
                                  Integer currentPage, Integer pageSize, boolean adminSearch) {
    }
}
