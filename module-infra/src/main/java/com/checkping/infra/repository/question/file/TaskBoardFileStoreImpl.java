package com.checkping.infra.repository.question.file;

import com.checkping.domain.question.Question;
import com.checkping.domain.question.QuestionFile;
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
     * @param question 업무 관리 게시글 ID
     * @param fileList  게시글 첨부 파일 리스트
     * @return List<QuestionFile>
     */
    @Override
    public List<QuestionFile> saveFileList(Question question, List<MultipartFile> fileList) {

        // S3 Upload
        List<FileRequest> uploadedFiles = fileRepository.uploadFiles(fileList);

        // File Dto -> Entity
        List<QuestionFile> files = uploadedFiles.stream()
            .map(request -> createTaskBoardFile(question, request))
            .toList();

        // Save Entity
        return taskBoardFileRepository.saveAll(files);
    }

    private QuestionFile createTaskBoardFile(Question question, FileRequest request) {
        return QuestionFile.builder()
            .originalName(request.originalName())
            .saveName(request.saveName())
            .url(request.url())
            .size(request.size())
            .question(question)
            .build();
    }
}
