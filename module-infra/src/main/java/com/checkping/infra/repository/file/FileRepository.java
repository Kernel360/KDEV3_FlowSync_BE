package com.checkping.infra.repository.file;

import com.checkping.common.utils.FileRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileRepository {
    List<FileRequest> uploadFiles(List<MultipartFile> multipartFiles);
    FileRequest uploadFile(MultipartFile multipartFile);
    void deleteFile(String saveName);
    String getPresignedUrl(String filename);
}
