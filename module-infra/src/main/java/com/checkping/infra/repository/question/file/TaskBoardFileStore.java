package com.checkping.infra.repository.question.file;

import com.checkping.domain.question.Question;
import com.checkping.domain.question.TaskBoardFile;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface TaskBoardFileStore {

    List<TaskBoardFile> saveFileList(Question question, List<MultipartFile> fileList);
}
