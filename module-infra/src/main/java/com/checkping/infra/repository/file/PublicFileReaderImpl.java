package com.checkping.infra.repository.file;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class PublicFileReaderImpl implements PublicFileReader {

    private final FileRepository fileRepository;

    public PublicFileReaderImpl(
        @Qualifier("s3PublicFileRepositoryImpl") FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public String getPresignedUrlToDownload(String fileName) {
        return fileRepository.getPresignedUrl(fileName);
    }
}
