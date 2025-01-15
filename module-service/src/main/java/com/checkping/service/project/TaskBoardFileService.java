package com.checkping.service.project;

import com.checkping.common.utils.FileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TaskBoardFileService {
    List<FileResponse> saveFiles(Long taskBoardId, List<MultipartFile> fileRequests);
}
