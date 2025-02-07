package com.checkping.infra.repository.file;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class FileReaderImpl implements FileReader {

    private final FileRepository fileRepository;

    public FileReaderImpl(@Qualifier("s3FileRepositoryImpl") FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public String  getPresignedUrlToDownload(String fileName) {
        return fileRepository.getPresignedUrl(fileName);
    }
}
