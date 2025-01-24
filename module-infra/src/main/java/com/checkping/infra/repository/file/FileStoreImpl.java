package com.checkping.infra.repository.file;

import com.checkping.common.utils.FileResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileStoreImpl implements FileStore {

    private final FileRepository fileRepository;

    public FileStoreImpl(@Qualifier("s3FileRepositoryImpl") FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public FileResponse upload(MultipartFile multipartFile) {
        return FileResponse.toDto(fileRepository.uploadFile(multipartFile));
    }

    @Override
    public List<FileResponse> uploadList(List<MultipartFile> multipartFiles) {
        return FileResponse.toResponseDtoList(fileRepository.uploadFiles(multipartFiles));
    }
}
