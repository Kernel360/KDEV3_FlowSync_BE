package com.checkping.common.utils;

import com.checkping.domain.board.TaskBoardFile;
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
     * TaskBoardFile Entity -> FileResponse Dto
     *
     * @param taskBoardFile TaskBoardFile Entity
     * @return FileResponse
     */
    public static FileResponse toDto(TaskBoardFile taskBoardFile) {
        return FileResponse.builder().originalName(taskBoardFile.getOriginalName())
            .saveName(taskBoardFile.getSaveName()).url(taskBoardFile.getUrl())
            .size(taskBoardFile.getSize()).build();
    }

    public static List<FileResponse> toDtoList(List<TaskBoardFile> taskBoardFiles) {
        if (taskBoardFiles == null || taskBoardFiles.isEmpty()) {
            return Collections.emptyList();
        }

        return taskBoardFiles.stream().map(FileResponse::toDto).toList();
    }
}
