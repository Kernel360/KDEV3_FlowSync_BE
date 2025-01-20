package com.checkping.infra.repository.question.file;

import com.checkping.domain.question.Question;
import com.checkping.domain.question.QuestionFile;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface QuestionFileStore {

    List<QuestionFile> saveFileList(Question question, List<MultipartFile> fileList);
}
