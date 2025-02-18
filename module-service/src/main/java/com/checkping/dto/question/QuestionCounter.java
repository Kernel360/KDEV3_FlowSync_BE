package com.checkping.dto.question;

import com.checkping.domain.project.ProgressStep;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionCounter {

    @Getter
    public static class Response {
        private Long id;
        private String title;
        private String value;
        private Long count;
        private Integer stepOrder;
        private String color;

        public static Response toDto(ProgressStep progressStep, Long count) {
            Response response = new Response();
            response.id = progressStep.getId();
            response.title = progressStep.getName();
            response.value = progressStep.getDescription();
            response.count = count;
            response.stepOrder = progressStep.getStepOrder();
            response.color = progressStep.getColor();
            return response;
        }

        /**
         * 전체 카운트 생성
         *
         * @param count 전체 카운트
         * @return  전체 카운트 Response
         */
        public static Response makeAllCount(Long count) {
            Response response = new Response();
            response.id = 0L;
            response.title = "전체";
            response.value = "ALL";
            response.count = count;
            response.stepOrder = -1;
            response.color = "#333333";
            return response;
        }
    }
}
