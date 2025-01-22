package com.checkping.service.file;

import com.checkping.common.utils.FileResponse;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    FileResponse upload(MultipartFile multipartFile);

    List<FileResponse> uploadFiles(List<MultipartFile> multipartFiles);
}
