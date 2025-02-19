package com.checkping.infra.repository.file;

import com.checkping.common.utils.FileResponse;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface PublicFileStore {

    FileResponse upload(MultipartFile multipartFile);

    List<FileResponse> uploadList(List<MultipartFile> multipartFiles);
}
