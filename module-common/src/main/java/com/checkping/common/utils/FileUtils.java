package com.checkping.common.utils;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUtils {
    List<TaskBoardFileRequest> uploadFiles(List<MultipartFile> multipartFiles);
    TaskBoardFileRequest uploadFile(MultipartFile multipartFile);
}
