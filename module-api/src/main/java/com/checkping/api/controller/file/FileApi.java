package com.checkping.api.controller.file;

import com.checkping.common.response.BaseResponse;
import com.checkping.common.utils.FileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "File API(FileController)", description = "파일 업로드 API 입니다.")
public interface FileApi {

    @Operation(summary = "파일 업로드", description = "파일을 업로드하는 기능입니다.")
    BaseResponse<FileResponse> uploadFile(
        @Parameter(description = "파일", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @RequestParam("file") MultipartFile multipartFile);
}
