package com.checkping.service.project;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TaskBoardFileService {
    void saveFiles(Long taskBoardId, List<MultipartFile> fileRequests);
}
