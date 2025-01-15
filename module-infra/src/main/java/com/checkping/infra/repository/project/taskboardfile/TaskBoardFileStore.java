package com.checkping.infra.repository.project.taskboardfile;

import com.checkping.domain.board.TaskBoard;
import com.checkping.domain.board.TaskBoardFile;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface TaskBoardFileStore {

    List<TaskBoardFile> saveFileList(TaskBoard taskBoard, List<MultipartFile> fileList);
}
