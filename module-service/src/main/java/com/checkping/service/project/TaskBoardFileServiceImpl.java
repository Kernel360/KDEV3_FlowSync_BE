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
public class TaskBoardFileServiceImpl implements TaskBoardFileService {
    private final TaskBoardFileRepository fileRepository;
    private final FileUtils fileUtils;

    public TaskBoardFileServiceImpl(TaskBoardFileRepository fileRepository, @Qualifier("fileUtilsS3") FileUtils fileUtils) {
        this.fileRepository = fileRepository;
        this.fileUtils = fileUtils;
    }

    @Override
    public void saveFiles(Long taskBoardId, List<MultipartFile> fileRequests) {
        List<FileRequest> uploadedFiles = fileUtils.uploadFiles(fileRequests);
        List<TaskBoardFile> files = uploadedFiles.stream()
                .map(request -> createTaskBoardFile(taskBoardId, request))
                .toList();
        fileRepository.saveAll(files);
    }

    private TaskBoardFile createTaskBoardFile(Long taskBoardId, FileRequest request) {
        return TaskBoardFile.builder()
                .originalName(request.originalName())
                .saveName(request.saveName())
                .url(request.url())
                .size(request.size())
                .taskBoardId(taskBoardId)
                .build();
    }
}
