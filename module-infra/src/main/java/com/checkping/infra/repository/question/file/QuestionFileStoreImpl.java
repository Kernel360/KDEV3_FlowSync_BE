package com.checkping.infra.repository.question.file;

import com.checkping.domain.question.Question;
import com.checkping.domain.question.QuestionFile;
import com.checkping.common.utils.FileRequest;
import com.checkping.infra.repository.file.FileRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class QuestionFileStoreImpl implements QuestionFileStore {

    private final QuestionFileRepository questionFileRepository;
    private final FileRepository fileRepository;

    public QuestionFileStoreImpl(QuestionFileRepository questionFileRepository,
        @Qualifier("s3FileRepositoryImpl") FileRepository fileRepository) {
        this.questionFileRepository = questionFileRepository;
        this.fileRepository = fileRepository;
    }

    /**
     * QuestionFileStore 첨부 파일 저장
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
            .map(request -> createQuestionFile(question, request))
            .toList();

        // Save Entity
        return questionFileRepository.saveAll(files);
    }

    /**
     * QuestionFileStore 첨부 파일 저장
     * S3 에는 저장하지 않는다.
     *
     * @param question 업무 관리 게시글 ID
     * @param fileList 게시글 첨부 파일 리스트
     * @return List<QuestionFile> DB 에 저장된 파일 정보 리스트
     */
    @Override
    public List<QuestionFile> storeFileList(Question question, List<FileRequest> fileList) {

        // File Dto -> Entity
        List<QuestionFile> files = fileList.stream()
            .map(request -> createQuestionFile(question, request))
            .toList();

        // Save Entity
        return questionFileRepository.saveAll(files);
    }

    private QuestionFile createQuestionFile(Question question, FileRequest request) {
        return QuestionFile.builder()
            .originalName(request.originalName())
            .saveName(request.saveName())
            .url(request.url())
            .size(request.size())
            .question(question)
            .build();
    }
}
