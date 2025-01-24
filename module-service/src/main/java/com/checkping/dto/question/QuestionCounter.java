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

        public static Response toDto(ProgressStep progressStep, Long count) {
            Response response = new Response();
            response.id = progressStep.getId();
            response.title = progressStep.getName();
            response.value = progressStep.getDescription();
            response.count = count;
            return response;
        }
    }
}
