package com.checkping.infra.repository.question.file;

import com.checkping.common.utils.FileRequest;
import com.checkping.domain.question.Question;
import com.checkping.domain.question.QuestionFile;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface QuestionFileStore {

    List<QuestionFile> saveFileList(Question question, List<MultipartFile> fileList);

    List<QuestionFile> storeFileList(Question question, List<FileRequest> fileList);

    QuestionFile store(QuestionFile file);

    List<QuestionFile> store(List<QuestionFile> files);
}
