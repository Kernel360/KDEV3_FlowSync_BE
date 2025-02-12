package com.checkping.service.member;

import com.checkping.common.dto.PageInfo;
import com.checkping.common.utils.FileResponse;
import com.checkping.dto.ProjectListGet;
import com.checkping.dto.member.request.ChangePasswordDto;
import com.checkping.dto.member.request.MemberRegisterDto;
import com.checkping.dto.member.request.MemberUpdateDto;
import com.checkping.dto.member.response.MemberListResponseDto;
import com.checkping.dto.member.response.MemberResponseDto;
import com.checkping.dto.member.response.MemberSignatureExistResponseDto;
import com.checkping.dto.member.response.MemberSignatureResponseDto;

public interface MemberService {

    MemberResponseDto getMemberById(Long memberId);

    MemberListResponseDto getAllMembersWithFilters(int page, int size, String roleParam, String statusParam, String keyword);

    MemberResponseDto registerMember(MemberRegisterDto dto);

    MemberResponseDto updateMember(Long memberId, MemberUpdateDto dto);

    void changePassword(Long memberId, ChangePasswordDto dto);

    void deleteMember(Long memberId, String reasonForDelete);

    MemberListResponseDto getMembersByOrganizationId(Long organizationId, int page, int size);

    MemberSignatureResponseDto uploadSignature(FileResponse signatureFile);

    MemberSignatureExistResponseDto getSignature();

    void activateMember(Long memberId);

    void deactivateMember(Long memberId);

    String getMemberStatus(Long memberId);

    PageInfo.Response<ProjectListGet.Response> getProjectsByMember(Long memberId, String managementStep, PageInfo.Request pageRequest);
}