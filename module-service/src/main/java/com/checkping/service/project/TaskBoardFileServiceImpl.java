package com.checkping.service.project;

import com.checkping.common.utils.FileResponse;
import com.checkping.domain.board.TaskBoard;
import com.checkping.domain.board.TaskBoardFile;
import com.checkping.exception.project.TaskBoardNotFoundEntityException;
import com.checkping.infra.repository.project.taskboard.TaskBoardReader;
import com.checkping.infra.repository.project.taskboardfile.TaskBoardFileStore;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class TaskBoardFileServiceImpl implements TaskBoardFileService {

    private final TaskBoardReader taskBoardReader;
    private final TaskBoardFileStore taskBoardFileStore;

    @Override
    public List<FileResponse> saveFiles(Long taskBoardId, List<MultipartFile> fileRequests) {

        TaskBoard taskBoard = taskBoardReader.getTaskBoardById(taskBoardId).orElseThrow(
            TaskBoardNotFoundEntityException::new);

        List<TaskBoardFile> files = taskBoardFileStore.saveFileList(taskBoard, fileRequests);

        return FileResponse.toDtoList(files);
    }
}
