package com.checkping.api.controller.file;

import com.checkping.common.response.BaseResponse;
import com.checkping.common.utils.FileResponse;
import com.checkping.service.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileController implements FileApi {

    private final FileService fileService;

    @PostMapping(value = "/file", consumes = {"multipart/form-data"})
    @Override
    public BaseResponse<FileResponse> uploadFile(
        @RequestParam("file") MultipartFile multipartFile) {
        return BaseResponse.success(fileService.upload(multipartFile));
    }
}
