package com.checkping.common.utils;

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
}
