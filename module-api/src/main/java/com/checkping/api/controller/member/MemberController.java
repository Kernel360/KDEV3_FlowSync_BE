package com.checkping.api.controller.member;

import static com.checkping.common.enums.SuccessCode.MEMBER_SIGNATURE_UPLOAD;

import com.checkping.common.response.BaseResponse;
import com.checkping.common.utils.FileResponse;
import com.checkping.dto.member.response.MemberSignatureExistResponseDto;
import com.checkping.dto.member.response.MemberSignatureResponseDto;
import com.checkping.service.file.FileService;
import com.checkping.service.member.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController implements MemberApi {

    private final MemberService memberService;
    private final FileService fileService;

    @Override
    @PostMapping(value = "/signatures", consumes = {"multipart/form-data"})
    public BaseResponse<MemberSignatureResponseDto> uploadSignature(
        @RequestParam("file") MultipartFile signature) {

        // 파일 업로드 처리
        FileResponse fileUpload = fileService.upload(signature);

        // 서명 파일 URL 저장
        MemberSignatureResponseDto response = memberService.uploadSignature(fileUpload);

        return BaseResponse.success(response, MEMBER_SIGNATURE_UPLOAD.getMessage());
    }

    @Override
    @GetMapping("/signatures")
    public BaseResponse<MemberSignatureExistResponseDto> isExistSignature() {

        // 서명 존재 여부 조회
        MemberSignatureExistResponseDto response = memberService.getSignature();

        return BaseResponse.success(response);
    }
}
