package com.checkping.api.controller;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.member.request.ChangePasswordDto;
import com.checkping.dto.member.request.MemberRegisterDto;
import com.checkping.dto.member.request.MemberUpdateDto;
import com.checkping.dto.member.response.MemberListResponseDto;
import com.checkping.dto.member.response.MemberResponseDto;
import com.checkping.service.member.MemberService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    //전체 회원 조회
    //GET /admins/members
    @GetMapping("/admins/members")
    public BaseResponse<MemberListResponseDto> getAllMembers() {
        MemberListResponseDto response = memberService.getAllMemberListAsDto();
        return BaseResponse.success(response);
    }

    //특정 고객 조회
    @GetMapping("/admins/members/{memberId}")
    public BaseResponse<MemberResponseDto> getMemberById(
            @PathVariable("memberId") UUID memberId
    ) {
        MemberResponseDto response = memberService.getMemberById(memberId);
        return BaseResponse.success(response);
    }

    //특정 고객 정보 수정
    //PUT /admins/members/{member_id}
    @PatchMapping("/admins/members/{member_id}")
    public BaseResponse<MemberResponseDto> updateMember(
            @PathVariable("member_id") UUID memberId,
            @RequestBody MemberUpdateDto dto
    ) {
        MemberResponseDto response = memberService.updateMember(memberId, dto);
        return BaseResponse.success(response);
    }

     // 특정 고객 비밀번호 변경
     // PATCH /admins/members/{member_id}/password
    @PatchMapping("/admins/members/{member_id}/password")
    public BaseResponse<String> changePassword(
            @PathVariable("member_id") UUID memberId,
            @RequestBody ChangePasswordDto dto
    ) {
        memberService.changePassword(memberId, dto);
        return BaseResponse.success("비밀 번호가 성공적으로 변경되었습니다!");
    }

    //회원 등록
    // POST /admins/members
    @PostMapping("/admins/members")
    public BaseResponse<MemberResponseDto> registerMember(
            @RequestBody MemberRegisterDto dto) {
        MemberResponseDto response = memberService.registerMember(dto);
        return BaseResponse.success(response);
    }

    @PostMapping("/admins/members/{memberId}/delete")
    public BaseResponse<String> deleteMember(
            @PathVariable("memberId") UUID memberId,
            @RequestBody String reason //탈퇴 사유
    ) {
        memberService.deleteMember(memberId, reason);
        return BaseResponse.success("회원이 성공적으로 삭제되었습니다.");
    }
}
