package com.checkping.api;

import com.checkping.api.controller.MemberController;
import com.checkping.dto.member.request.ChangePasswordDto;
import com.checkping.dto.member.request.MemberRegisterDto;
import com.checkping.dto.member.request.MemberUpdateDto;
import com.checkping.dto.member.response.MemberListResponseDto;
import com.checkping.dto.member.response.MemberResponseDto;
import com.checkping.service.member.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.BDDMockito; // static import 시 BDDMockito.*
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

    // (1) Controller에 바로 @InjectMocks
    @InjectMocks
    private MemberController memberController;

    // (2) Service를 @Mock
    @Mock
    private MemberService memberService;

    // (3) MockMvc: @WebMvcTest 없이 직접 빌드
    private MockMvc mockMvc;

    // (4) JSON 직렬화/역직렬화용
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8) // 추가
                .build();
    }

    @Test
    @DisplayName("회원 등록 - 성공 케이스")
    void registerMember() throws Exception {
        // given
        MemberResponseDto mockResponse = MemberResponseDto.builder()
                .id(UUID.randomUUID())
                .email("mock@test.com")
                .name("MockUser")
                .build();

        BDDMockito.given(memberService.registerMember(any(MemberRegisterDto.class)))
                .willReturn(mockResponse);

        // request DTO
        MemberRegisterDto requestDto = new MemberRegisterDto();
        requestDto.setOrganizationId(UUID.randomUUID());
        requestDto.setEmail("mock@test.com");
        requestDto.setPassword("rawPassword");
        requestDto.setName("MockUser");
        requestDto.setPhoneNum("010-1234-5678");

        // when & then
        mockMvc.perform(post("/admins/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("mock@test.com"))
                .andExpect(jsonPath("$.name").value("MockUser"));
    }

    @Test
    @DisplayName("회원 조회 - 특정 ID + 이메일로 조회")
    void getMemberById() throws Exception {
        UUID memberId = UUID.randomUUID();
        MemberResponseDto mockResponse = MemberResponseDto.builder()
                .id(memberId)
                .email("emailLookup@test.com")
                .name("조회테스터")
                .build();

        // email 파라미터가 "emailLookup@test.com"일 때 mockResponse 반환
        BDDMockito.given(memberService.getMemberByEmail("emailLookup@test.com"))
                .willReturn(mockResponse);

        mockMvc.perform(get("/admins/members/{member_id}", memberId)
                        .param("email", "emailLookup@test.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(memberId.toString()))
                .andExpect(jsonPath("$.email").value("emailLookup@test.com"))
                .andExpect(jsonPath("$.name").value("조회테스터"));
    }

    @Test
    @DisplayName("회원 정보 수정")
    void updateMember() throws Exception {
        UUID memberId = UUID.randomUUID();
        MemberResponseDto mockResponse = MemberResponseDto.builder()
                .id(memberId)
                .email("before@test.com")
                .name("수정된 이름")
                .build();

        BDDMockito.given(memberService.updateMember(eq(memberId), any(MemberUpdateDto.class)))
                .willReturn(mockResponse);

        MemberUpdateDto updateDto = new MemberUpdateDto();
        updateDto.setName("수정된 이름");
        updateDto.setPhoneNum("010-9999-9999");

        mockMvc.perform(put("/admins/members/{member_id}", memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(memberId.toString()))
                .andExpect(jsonPath("$.name").value("수정된 이름"));
    }



    @Test
    @DisplayName("전체 회원 조회")
    void getAllMembers() throws Exception {
        MemberListResponseDto mockListDto = new MemberListResponseDto(Collections.emptyList());

        BDDMockito.given(memberService.getAllMemberListAsDto())
                .willReturn(mockListDto);

        mockMvc.perform(get("/admins/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.members").isArray());
    }


    @Test
    @DisplayName("비밀번호 변경 - 성공 케이스")
    void changePasswordSuccess() throws Exception {
        // given
        UUID memberId = UUID.randomUUID();

        // 비밀번호 변경 요청 DTO
        ChangePasswordDto changeDto = new ChangePasswordDto();
        changeDto.setCurrentPassword("oldPw");
        changeDto.setNewPassword("newPw123");
        changeDto.setConfirmNewPassword("newPw123");

        // Service 동작: 성공 시 아무 예외도 없으므로 willDoNothing()
        BDDMockito.willDoNothing()
                .given(memberService)
                .changePassword(eq(memberId), any(ChangePasswordDto.class));

        // when & then
        mockMvc.perform(patch("/admins/members/{member_id}/password", memberId)
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Your password has been changed."));
    }

    //TODO : 비밀번호 변경 실패 케이스 테스트 코드 작성
}
