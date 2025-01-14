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

    //특정 고객 조회 (이메일로 조회 가능, 엔드포인트는 ID 형태 유지)
    //GET /admins/members/{member_id}?email=xxx@example.com
    @GetMapping("/admins/members/{member_id}") // TODO 언더바 잘 안씀
    public BaseResponse<MemberResponseDto> getMemberById(
            @PathVariable("member_id") UUID memberId, // TODO URL 설계 다시하기
            @RequestParam String email
    ) {
        // 현재 구현에서는 email을 이용해 조회 (memberId는 URI 형태상 포함)
        MemberResponseDto response = memberService.getMemberByEmail(email);  // TODO Dto라는 파일 이름 뒤에 잘 안붙임, 굳이 더 안쓸 변수 response 말고 바로 반환하기
        return BaseResponse.success(response);
    }

    //특정 고객 정보 수정
    //PUT /admins/members/{member_id} //TODO PATCH로 변경, 부분 정보 수정이기 떄문에
    @PutMapping("/admins/members/{member_id}")
    public BaseResponse<MemberResponseDto> updateMember(
            @PathVariable("member_id") UUID memberId,
            @RequestBody MemberUpdateDto dto
    ) {
        MemberResponseDto response = memberService.updateMember(memberId, dto);
        return BaseResponse.success(response);
    }

     // 특정 고객 비밀번호 변경
     // PATCH /admins/members/{member_id}/password
    // TODO: 한글이 꺠져서 예외 메시지 영어로 변경함, 한글 깨지는 문제 해결
    @PatchMapping("/admins/members/{member_id}/password")
    public BaseResponse<String> changePassword(
            @PathVariable("member_id") UUID memberId,
            @RequestBody ChangePasswordDto dto
    ) {
        memberService.changePassword(memberId, dto);
        return BaseResponse.success("Your password has been changed.");
    }

    //전체 회원 조회
    //GET /admins/members
    @GetMapping("/admins/members")
    public BaseResponse<MemberListResponseDto> getAllMembers() {
        MemberListResponseDto response = memberService.getAllMemberListAsDto();
        return BaseResponse.success(response);
    }


    //회원 등록
    // POST /admins/members
    @PostMapping("/admins/members")
    public BaseResponse<MemberResponseDto> registerMember(@RequestBody MemberRegisterDto dto) {
        MemberResponseDto response = memberService.registerMember(dto);
        return BaseResponse.success(response);
    }
}
