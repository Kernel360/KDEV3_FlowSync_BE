package com.checkping.common.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FileUtilsS3 implements FileUtils {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;


    /**
     * 다중 파일 업로드
     *
     * @param multipartFiles - 파일 객체 List
     * @return DB에 저장할 파일 정보 List
     */
    @Override
    public List<FileRequest> uploadFiles(List<MultipartFile> multipartFiles) {

        List<FileRequest> fileRequests = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (multipartFile.isEmpty())
                continue;

            FileRequest fileRequest = uploadFile(multipartFile);
            fileRequests.add(fileRequest);
        }

        return fileRequests;
    }

    /**
     * 단일 파일 업로드
     *
     * @param multipartFile - 파일 객체
     * @return DB에 저장할 파일 정보
     */
    @Override
    public FileRequest uploadFile(MultipartFile multipartFile) {
        // S3 업로드 요청
        String originalName = multipartFile.getOriginalFilename();

        String saveName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
        String uploadPath = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + saveName;
        long size = multipartFile.getSize();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(saveName)
                .build();

        // MultipartFile을 임시 파일로 변환
        File tempFile = null;
        try {
            tempFile = convertMultiPartToFile(multipartFile);
            PutObjectResponse response = s3Client.putObject(request, RequestBody.fromFile(tempFile.toPath()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 임시 파일 삭제
            tempFile.delete();
        }

        // 업로드한 파일의 URL 반환
        return FileRequest.builder()
                .originalName(originalName)
                .saveName(saveName)
                .url(uploadPath)
                .size(size)
                .build();
    }

    /**
     * 멀티파트 파일 -> 파일로 변환
     *
     * @param file - 파일 객체
     * @return 변환된 파일 객체를 반환
     */
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        }
        return convertedFile;
    }
}