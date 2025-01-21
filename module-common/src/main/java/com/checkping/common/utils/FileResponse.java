package com.checkping.common.utils;

import com.checkping.domain.question.QuestionFile;
import java.util.Collections;
import java.util.List;
import lombok.Builder;

/**
 * @param originalName 원본 파일명
 * @param saveName     저장 파일명
 * @param url          업로드 경로명
 * @param size         파일 크기
 */
public record FileResponse(String originalName, String saveName, String url, long size) {

    @Builder
    public FileResponse {
    }

    /**
     * QuestionFile Entity -> FileResponse Dto
     *
     * @param questionFile QuestionFile Entity
     * @return FileResponse
     */
    public static FileResponse toDto(QuestionFile questionFile) {
        return FileResponse.builder().originalName(questionFile.getOriginalName())
            .saveName(questionFile.getSaveName()).url(questionFile.getUrl())
            .size(questionFile.getSize()).build();
    }

    public static List<FileResponse> toDtoList(List<QuestionFile> questionFiles) {
        if (questionFiles == null || questionFiles.isEmpty()) {
            return Collections.emptyList();
        }

        return questionFiles.stream().map(FileResponse::toDto).toList();
    }
}
