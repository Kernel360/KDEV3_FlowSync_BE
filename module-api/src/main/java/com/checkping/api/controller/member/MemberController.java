package com.checkping.api.controller.member;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.member.request.ChangePasswordDto;
import com.checkping.dto.member.request.MemberRegisterDto;
import com.checkping.dto.member.request.MemberUpdateDto;
import com.checkping.dto.member.response.MemberListResponseDto;
import com.checkping.dto.member.response.MemberResponseDto;
import com.checkping.service.member.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "회원 관리 API", description = "회원 관리 관련 REST API")
@RestController
@RequestMapping("/admins/members")
public class MemberController implements MemberApi {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    @PostMapping
    public BaseResponse<MemberResponseDto> registerMember(@RequestBody MemberRegisterDto request) {
        MemberResponseDto response = memberService.registerMember(request);
        return BaseResponse.success(response);
    }

    @Override
    @GetMapping
    public BaseResponse<MemberListResponseDto> getAllMembers() {
        MemberListResponseDto response = memberService.getAllMemberListAsDto();
        return BaseResponse.success(response);
    }

    @Override
    @GetMapping("/{memberId}")
    public BaseResponse<MemberResponseDto> getMemberById(@PathVariable UUID memberId) {
        MemberResponseDto response = memberService.getMemberById(memberId);
        return BaseResponse.success(response);
    }

    @Override
    @PatchMapping("/{memberId}")
    public BaseResponse<MemberResponseDto> updateMember(
            @PathVariable UUID memberId,
            @RequestBody MemberUpdateDto request) {
        MemberResponseDto response = memberService.updateMember(memberId, request);
        return BaseResponse.success(response);
    }

    @Override
    @PatchMapping("/{memberId}/password")
    public BaseResponse<String> changePassword(
            @PathVariable UUID memberId,
            @RequestBody ChangePasswordDto request) {
        memberService.changePassword(memberId, request);
        return BaseResponse.success("비밀번호가 성공적으로 변경되었습니다!");
    }

    @Override
    @PostMapping("/delete/{memberId}")
    public BaseResponse<String> deleteMember(
            @PathVariable UUID memberId,
            @RequestBody String reason) {
        memberService.deleteMember(memberId, reason);
        return BaseResponse.success("회원이 성공적으로 삭제되었습니다.");
    }
}