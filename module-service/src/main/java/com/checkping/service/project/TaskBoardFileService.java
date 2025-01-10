package com.checkping.service.project;

import com.checkping.common.utils.FileRequest;
import com.checkping.common.utils.FileUtils;
import com.checkping.domain.board.TaskBoardFile;
import com.checkping.infra.repository.project.TaskBoardFileRepository;
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
        List<FileRequest> uploadedFiles = fileUtils.uploadFiles(fileRequests);
        for (FileRequest request : uploadedFiles) {
            TaskBoardFile file = TaskBoardFile.builder()
                    .originalName(request.originalName())
                    .saveName(request.saveName())
                    .url(request.url())
                    .size(request.size())
                    .taskBoardId(taskBoardId)
                    .build();
            file.setTaskBoardId(taskBoardId);
            fileRepository.save(file);
        }
    }
}
