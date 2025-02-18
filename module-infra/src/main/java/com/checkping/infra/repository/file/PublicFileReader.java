package com.checkping.infra.repository.file;

public interface PublicFileReader {

    String getPresignedUrlToDownload(String fileName);
}
