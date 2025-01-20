package com.checkping.service.question.file;

import com.checkping.common.utils.FileResponse;
import com.checkping.domain.question.Question;
import com.checkping.domain.question.QuestionFile;
import com.checkping.exception.question.QuestionNotFoundEntityException;
import com.checkping.infra.repository.question.QuestionReader;
import com.checkping.infra.repository.question.file.QuestionFileStore;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class TaskBoardFileServiceImpl implements TaskBoardFileService {

    private final QuestionReader questionReader;
    private final QuestionFileStore questionFileStore;

    @Override
    public List<FileResponse> saveFiles(Long taskBoardId, List<MultipartFile> fileRequests) {

        Question question = questionReader.getTaskBoardById(taskBoardId).orElseThrow(
            QuestionNotFoundEntityException::new);

        List<QuestionFile> files = questionFileStore.saveFileList(question, fileRequests);

        return FileResponse.toDtoList(files);
    }
}
