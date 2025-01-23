package com.checkping.dto.question.file;

import com.checkping.domain.question.QuestionFile;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionFileRegister {

    @Getter
    public static class Response {

        /*
        id : id
        questionId : 게시글 번호
        originalName : 원본 파일명
        saveName : 저장 파일명
        url : 저장 URL
        size : 파일 Size
         */
        private Long id;
        private Long questionId;
        private String originalName;
        private String saveName;
        private String url;
        private long size;

        /**
         * QuestionFile Entity -> Response Dto
         *
         * @param questionFile QuestionFile Entity
         * @return Response Dto
         */
        public static Response toDto(QuestionFile questionFile) {
            Response response = new Response();
            response.id = questionFile.getId();
            response.questionId = questionFile.getQuestion().getId();
            response.originalName = questionFile.getOriginalName();
            response.saveName = questionFile.getSaveName();
            response.url = questionFile.getUrl();
            response.size = questionFile.getSize();
            return response;
        }

        /**
         * QuestionFile Entity List -> Response Dto List
         *
         * @param questionFiles QuestionFile Entity List
         * @return Response Dto List
         */
        public static List<Response> toDto(List<QuestionFile> questionFiles) {
            // Check null or empty
            if (questionFiles == null || questionFiles.isEmpty()) {
                return List.of();
            }

            return questionFiles.stream()
                .map(QuestionFileRegister.Response::toDto)
                .toList();
        }
    }
}
