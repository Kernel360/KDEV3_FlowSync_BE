package com.checkping.service.question.file;

import com.checkping.common.utils.FileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface QuestionFileService {
    List<FileResponse> saveFiles(Long taskBoardId, List<MultipartFile> fileRequests);
}
