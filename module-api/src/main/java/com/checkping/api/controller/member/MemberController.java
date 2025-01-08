package com.checkping.api.controller.member;

import com.checkping.service.Member.MemberService;
import com.checkping.service.Member.dto.request.ChangePasswordDto;
import com.checkping.service.Member.dto.request.MemberRegisterDto;
import com.checkping.service.Member.dto.request.MemberUpdateDto;
import com.checkping.service.Member.dto.response.MemberListResponseDto;
import com.checkping.service.Member.dto.response.MemberResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 특정 고객 조회 (이메일로 조회 가능, 엔드포인트는 ID 형태 유지)
    @GetMapping("/admins/members/{member_id}")
    public ResponseEntity<MemberResponseDto> getMemberById(@PathVariable UUID member_id, @RequestParam String email) {
        // 이메일을 서비스 계층에 전달하여 처리
        MemberResponseDto response = memberService.getMemberByEmail(email);
        return ResponseEntity.ok(response);
    }

    // 특정 고객 정보 수정
    @PutMapping("/admins/members/{member_id}")
    public ResponseEntity<MemberResponseDto> updateMember(
            @PathVariable UUID member_id,
            @RequestBody MemberUpdateDto dto) {
        MemberResponseDto response = memberService.updateMember(member_id, dto);
        return ResponseEntity.ok(response);
    }

    // 특정 고객 비밀번호 변경
    @PatchMapping("/admins/members/{member_id}/password")
    public ResponseEntity<String> changePassword(
            @PathVariable UUID member_id,
            @RequestBody ChangePasswordDto dto) {
        memberService.changePassword(member_id, dto);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }

    // 전체 회원 조회
    @GetMapping("/admins/members")
    public ResponseEntity<MemberListResponseDto> getAllMembers() {
        MemberListResponseDto response = memberService.getAllMemberListAsDto();
        return ResponseEntity.ok(response);
    }

    // 회원 생성
    @PostMapping("/admins/members")
    public ResponseEntity<MemberResponseDto> registerMember(@RequestBody MemberRegisterDto dto) {
        MemberResponseDto response = memberService.registerMember(dto);
        return ResponseEntity.ok(response);
    }
}