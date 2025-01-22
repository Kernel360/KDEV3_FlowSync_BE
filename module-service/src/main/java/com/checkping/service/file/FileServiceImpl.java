package com.checkping.service.file;

import com.checkping.common.utils.FileResponse;
import com.checkping.infra.repository.file.FileStore;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileStore fileStore;

    @Override
    public FileResponse upload(MultipartFile multipartFile) {
        return fileStore.upload(multipartFile);
    }

    @Override
    public List<FileResponse> uploadFiles(List<MultipartFile> multipartFiles) {
        return fileStore.uploadList(multipartFiles);
    }
}
