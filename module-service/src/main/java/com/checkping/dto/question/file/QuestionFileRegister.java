package com.checkping.dto.question.file;

import com.checkping.common.utils.FileRequest;
import com.checkping.domain.question.Question;
import com.checkping.domain.question.QuestionFile;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionFileRegister {

    @Getter
    public static class Request {

        /*
        originalName : 원본 파일명
        saveName : 저장 파일명
        url : 저장 URL
        size : 파일 Size
         */
        private String originalName;
        private String saveName;
        private String url;
        private long size;

        /**
         * FileRequest Dto -> QuestionFile Entity
         *
         * @param question Question Entity
         * @param request  FileRequest Dto (첨부 파일)
         * @return QuestionFile Entity
         */
        public static QuestionFile toEntity(Question question, FileRequest request) {

            return QuestionFile.generate(question, request.originalName(), request.saveName(),
                request.url(), request.size());
        }


        /**
         * FileRequest Dto List -> QuestionFile Entity List
         *
         * @param question Question Entity
         * @param requests FileRequest Dto List (첨부 파일 리스트)
         * @return List<QuestionFile> Entity List
         */
        public static List<QuestionFile> toEntity(Question question,
            List<FileRequest> requests) {

            // Check null or empty
            if (requests == null || requests.isEmpty()) {
                return List.of();
            }

            return requests.stream().map(request -> toEntity(question, request)).toList();
        }
    }

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

            return questionFiles.stream().map(QuestionFileRegister.Response::toDto).toList();
        }

        /**
         * Set PreSignedUrl
         *
         * @param preSignedUrl  PreSignedUrl
         */
        public void setPreSignedUrl(String preSignedUrl) {
            this.url = preSignedUrl;
        }
    }
}
