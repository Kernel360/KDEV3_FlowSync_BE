package com.checkping.api.controller.member;

import com.checkping.service.Member.MemberService;
import com.checkping.service.Member.dto.request.ChangePasswordDto;
import com.checkping.service.Member.dto.request.MemberRegisterDto;
import com.checkping.service.Member.dto.request.MemberUpdateDto;
import com.checkping.service.Member.dto.response.MemberListResponseDto;
import com.checkping.service.Member.dto.response.MemberResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MemberControllerTest {

    private MockMvc mockMvc;
    private MemberService memberService;
    private MemberResponseDto sampleResponseDto;
    private MemberListResponseDto sampleListResponseDto;

    @BeforeEach
    void setUp() {
        // Mock MemberService 생성
        memberService = Mockito.mock(MemberService.class);

        // MockMvc에 MemberController를 설정
        mockMvc = MockMvcBuilders.standaloneSetup(new MemberController(memberService)).build();

        // 테스트 데이터를 준비
        UUID organizationId = UUID.randomUUID();
        sampleResponseDto = MemberResponseDto.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .email("test@example.com")
                .phoneNum("01012345678")
                .jobRole("Developer")
                .jobTitle("Engineer")
                .type("MEMBER")
                .status("ACTIVE")
                .organizationId(organizationId)
                .remark("Sample remark")
                .build();

        List<MemberResponseDto> members = new ArrayList<>();
        members.add(sampleResponseDto);
        sampleListResponseDto = MemberListResponseDto.builder()
                .members(members)
                .build();
    }

    @Test
    void getMemberById_Success() throws Exception {
        UUID memberId = UUID.randomUUID();
        String email = "test@example.com";
        when(memberService.getMemberByEmail(email)).thenReturn(sampleResponseDto);

        mockMvc.perform(get("/admins/members/{member_id}", memberId)
                        .param("email", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value(email));

        verify(memberService, times(1)).getMemberByEmail(email);
    }

    @Test
    void updateMember_Success() throws Exception {
        UUID memberId = UUID.randomUUID();
        MemberUpdateDto updateDto = new MemberUpdateDto();
        updateDto.setName("Updated Name");
        updateDto.setPhoneNum("01098765432");
        updateDto.setJobRole("Manager");
        updateDto.setJobTitle("Senior Manager");
        updateDto.setRemark("Updated remark");

        when(memberService.updateMember(eq(memberId), any(MemberUpdateDto.class))).thenReturn(sampleResponseDto);

        mockMvc.perform(put("/admins/members/{member_id}", memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Updated Name",
                                  "phoneNum": "01098765432",
                                  "jobRole": "Manager",
                                  "jobTitle": "Senior Manager",
                                  "remark": "Updated remark"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(memberService, times(1)).updateMember(eq(memberId), any(MemberUpdateDto.class));
    }

    @Test
    void changePassword_Success() throws Exception {
        // Given
        UUID memberId = UUID.randomUUID();
        ChangePasswordDto changePasswordDto = new ChangePasswordDto();
        changePasswordDto.setCurrentPassword("oldPassword");
        changePasswordDto.setNewPassword("newPassword123");
        changePasswordDto.setConfirmNewPassword("newPassword123");

        doNothing().when(memberService).changePassword(eq(memberId), any(ChangePasswordDto.class));

        // When & Then
        mockMvc.perform(patch("/admins/members/{memberId}/password", memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON) // UTF-8 설정
                        .content("""
                        {
                          "currentPassword": "oldPassword",
                          "newPassword": "newPassword123",
                          "confirmNewPassword": "newPassword123"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(content().string("비밀번호가 성공적으로 변경되었습니다.")); // 예상 결과와 일치하는지 검증

        verify(memberService, times(1)).changePassword(eq(memberId), any(ChangePasswordDto.class));
    }

    @Test
    void getAllMembers_Success() throws Exception {
        when(memberService.getAllMemberListAsDto()).thenReturn(sampleListResponseDto);

        mockMvc.perform(get("/admins/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.members").isArray())
                .andExpect(jsonPath("$.members[0].name").value("Test User"));

        verify(memberService, times(1)).getAllMemberListAsDto();
    }

    @Test
    void registerMember_Success() throws Exception {
        MemberRegisterDto registerDto = new MemberRegisterDto();
        registerDto.setName("Test User");
        registerDto.setEmail("test@example.com");
        registerDto.setPhoneNum("01012345678");
        registerDto.setType("MEMBER");
        registerDto.setStatus("ACTIVE");

        when(memberService.registerMember(any(MemberRegisterDto.class))).thenReturn(sampleResponseDto);

        mockMvc.perform(post("/admins/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Test User",
                                  "email": "test@example.com",
                                  "phoneNum": "01012345678",
                                  "type": "MEMBER",
                                  "status": "ACTIVE"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(memberService, times(1)).registerMember(any(MemberRegisterDto.class));
    }
}