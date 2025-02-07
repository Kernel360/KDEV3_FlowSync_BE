package com.checkping.infra.repository.file;

public interface FileReader {
    String getPresignedUrlToDownload(String fileName);
}
