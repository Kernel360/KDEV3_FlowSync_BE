package com.checkping.common.utils;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUtils {
    List<FileRequest> uploadFiles(List<MultipartFile> multipartFiles);
    FileRequest uploadFile(MultipartFile multipartFile);
    public void deleteFile(String saveName);
}
