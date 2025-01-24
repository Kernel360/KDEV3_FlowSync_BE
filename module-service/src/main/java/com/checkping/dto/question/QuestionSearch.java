package com.checkping.dto.question;

import com.checkping.common.response.PaginationProps;
import com.checkping.domain.question.Question;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionSearch {

    @Getter
    public static class QuestionItem {

        /*
        id : 게시글 ID
        title : 게시글 제목
        progressStepId : 게시글 진행 단계 ID
        category : 게시글 유형
        status : 게시글 상태
        createdDate : 게시글 생성일
         */
        private Long id;
        private String title;
        // TODO : progressStepId 는 ProgressStep Entity 으로 변경할 에정
        private Long progressStepId;
        private String category;
        private String status;
        private String createdDate;

        public static QuestionItem toDto(Question question) {
            QuestionItem dto = new QuestionItem();
            dto.id = question.getId();
            dto.title = question.getTitle();
            dto.progressStepId = question.getProgressStepId();
            dto.category = question.getCategory().name();
            dto.status = question.getStatus().name();
            dto.createdDate = question.getCreatedBy();
            return dto;
        }

        public static List<QuestionItem> toDto(List<Question> questions) {
            if (questions == null || questions.isEmpty()) {
                return List.of();
            }

            return questions.stream().map(QuestionItem::toDto).toList();
        }
    }

    @Getter
    public static class Response {
        /*
        items : 게시글 목록
        meta : 페이징 정보
         */

        private List<QuestionItem> projectQuestions;
        private PaginationProps meta;

        public static Response toDto(Page<Question> page) {
            Response response = new Response();
            response.projectQuestions = QuestionItem.toDto(page.getContent());
            response.meta = PaginationProps.toDto(page);
            return response;
        }
    }
}
