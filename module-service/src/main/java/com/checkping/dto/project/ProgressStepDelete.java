package com.checkping.dto.project;

import com.checkping.domain.project.ProgressStep;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgressStepDelete {

    public static class Response {
        /*
        id : 삭제된 progress step id
         */
        private Long id;

        public static Response toDto(ProgressStep progressStep) {
            Response dto = new Response();
            dto.id = progressStep.getId();
            return dto;
        }
    }

}
