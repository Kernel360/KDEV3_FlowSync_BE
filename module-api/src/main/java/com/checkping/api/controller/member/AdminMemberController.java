package com.checkping.api.controller.member;

import com.checkping.common.dto.PageInfo;
import com.checkping.common.response.BaseResponse;
import com.checkping.dto.ProjectListGet;
import com.checkping.dto.member.request.ChangePasswordDto;
import com.checkping.dto.member.request.MemberRegisterDto;
import com.checkping.dto.member.request.MemberUpdateDto;
import com.checkping.dto.member.response.MemberListResponseDto;
import com.checkping.dto.member.response.MemberResponseDto;
import com.checkping.service.member.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "어드민 회원 관리 API(AdminMemberApi)", description = "어드민 권한으로 회원을 관리할 수 있도록 하는 API입니다.")
@RestController
@RequestMapping("/admins/members")
public class AdminMemberController implements AdminMemberApi {

    private final MemberService memberService;

    public AdminMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    //keyword(예: 이름/이메일 검색)
    @GetMapping
    public BaseResponse<MemberListResponseDto> getAllMembers(
            @RequestParam(defaultValue = "1") int currentPage,  // 1부터 시작
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword
    ) {
        // Service layer로 전달 시 0-based index로 맞춰줌
        MemberListResponseDto response = memberService.getAllMembersWithFilters(
                currentPage - 1,
                pageSize,
                role,
                status,
                keyword
        );
        return BaseResponse.success(response);
    }


    @Override
    @PostMapping
    public BaseResponse<MemberResponseDto> registerMember(@RequestBody MemberRegisterDto request) {
        MemberResponseDto response = memberService.registerMember(request);
        return BaseResponse.success(response);
    }

    @Override
    @GetMapping("/{memberId}")
    public BaseResponse<MemberResponseDto> getMemberById(@PathVariable Long memberId) {
        MemberResponseDto response = memberService.getMemberById(memberId);
        return BaseResponse.success(response);
    }

    @Override
    @PatchMapping("/{memberId}")
    public BaseResponse<MemberResponseDto> updateMember(
            @PathVariable Long memberId,
            @RequestBody MemberUpdateDto request
    ) {
        MemberResponseDto response = memberService.updateMember(memberId, request);
        return BaseResponse.success(response);
    }

    @Override
    @PatchMapping("/{memberId}/password")
    public BaseResponse<String> changePassword(
            @PathVariable Long memberId,
            @RequestBody ChangePasswordDto request
    ) {
        memberService.changePassword(memberId, request);
        return BaseResponse.success("비밀번호가 성공적으로 변경되었습니다!");
    }

    @Override
    @PostMapping("/delete/{memberId}")
    public BaseResponse<String> deleteMember(
            @PathVariable Long memberId,
            @RequestBody String reason
    ) {
        memberService.deleteMember(memberId, reason);
        return BaseResponse.success("회원이 성공적으로 삭제되었습니다.");
    }

    @Override
    @GetMapping("/member/org/{organizationId}")
    public BaseResponse<MemberListResponseDto> getMembersByOrganizationId(
            @PathVariable Long organizationId,
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize) {
        MemberListResponseDto response = memberService.getMembersByOrganizationId(organizationId, currentPage-1, pageSize);
        return BaseResponse.success(response);
    }

    //회원 활성화
    @Override
    @PostMapping("/activate")
    public BaseResponse<String> activateMember(@RequestParam Long memberId) {
        memberService.activateMember(memberId);
        return BaseResponse.success("회원이 성공적으로 활성화되었습니다.");
    }

    //회원 비활성화
    @Override
    @PostMapping("/deactivate")
    public BaseResponse<String> deactivateMember(@RequestParam Long memberId) {
        memberService.deactivateMember(memberId);
        return BaseResponse.success("회원이 성공적으로 비활성화되었습니다.");
    }

    @Override
    @GetMapping("/{memberId}/projects")
    public BaseResponse<PageInfo.Response<ProjectListGet.Response>> getProjectsByMember(
            @PathVariable Long memberId,
            @RequestParam(required = false) String managementStep,
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword) {

        PageInfo.Request request = new PageInfo.Request(currentPage, pageSize, keyword);

        PageInfo.Response<ProjectListGet.Response> list = memberService.getProjectsByMember(memberId, managementStep, request);

        return BaseResponse.success(list, "회원이 속한 프로젝트 목록 조회 성공");
    }
}