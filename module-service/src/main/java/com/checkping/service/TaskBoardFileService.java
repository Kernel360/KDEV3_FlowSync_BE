package com.checkping.service;

import com.checkping.common.utils.TaskBoardFileRequest;
import com.checkping.common.utils.FileUtils;
import com.checkping.domain.TaskBoardFile;
import com.checkping.infra.repository.TaskBoardFileRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class TaskBoardFileService {
    private final TaskBoardFileRepository fileRepository;
    private final FileUtils fileUtils;

    public TaskBoardFileService(TaskBoardFileRepository fileRepository, @Qualifier("fileUtilsS3") FileUtils fileUtils) {
        this.fileRepository = fileRepository;
        this.fileUtils = fileUtils;
    }

    public void saveFiles(Long taskBoardId, List<MultipartFile> fileRequests) {
        List<TaskBoardFileRequest> uploadedFiles = fileUtils.uploadFiles(fileRequests);

        for (TaskBoardFileRequest request : uploadedFiles) {
            TaskBoardFile file = TaskBoardFileRequest.toEntity(request);
            file.setTaskBoardId(taskBoardId);
            fileRepository.save(file);
        }
    }
}
