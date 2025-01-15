package com.checkping.infra.repository.project.taskboardfile;

import com.checkping.domain.board.TaskBoard;
import com.checkping.domain.board.TaskBoardFile;
import com.checkping.common.utils.FileRequest;
import com.checkping.infra.repository.file.FileRepository;
import com.checkping.infra.repository.project.TaskBoardFileRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class TaskBoardFileStoreImpl implements TaskBoardFileStore {

    private final TaskBoardFileRepository taskBoardFileRepository;
    private final FileRepository fileRepository;

    public TaskBoardFileStoreImpl(TaskBoardFileRepository taskBoardFileRepository,
        @Qualifier("s3FileRepositoryImpl") FileRepository fileRepository) {
        this.taskBoardFileRepository = taskBoardFileRepository;
        this.fileRepository = fileRepository;
    }

    /**
     * TaskBoardFileStore 첨부 파일 저장
     *
     * @param taskBoard 업무 관리 게시글 ID
     * @param fileList  게시글 첨부 파일 리스트
     * @return List<TaskBoardFile>
     */
    @Override
    public List<TaskBoardFile> saveFileList(TaskBoard taskBoard, List<MultipartFile> fileList) {

        // S3 Upload
        List<FileRequest> uploadedFiles = fileRepository.uploadFiles(fileList);

        // File Dto -> Entity
        List<TaskBoardFile> files = uploadedFiles.stream()
            .map(request -> createTaskBoardFile(taskBoard, request))
            .toList();

        // Save Entity
        return taskBoardFileRepository.saveAll(files);
    }

    private TaskBoardFile createTaskBoardFile(TaskBoard taskBoard, FileRequest request) {
        return TaskBoardFile.builder()
            .originalName(request.originalName())
            .saveName(request.saveName())
            .url(request.url())
            .size(request.size())
            .taskBoard(taskBoard)
            .build();
    }
}
