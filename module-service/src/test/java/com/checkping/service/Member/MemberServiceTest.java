package com.checkping.service.Member;

import com.checkping.domain.member.Member;
import com.checkping.domain.member.Organization;
import com.checkping.infra.repository.MemberRepository;
import com.checkping.infra.repository.OrganizationRepository;
import com.checkping.service.Member.dto.request.ChangePasswordDto;
import com.checkping.service.Member.dto.request.MemberRegisterDto;
import com.checkping.service.Member.dto.request.MemberUpdateDto;
import com.checkping.service.Member.dto.response.MemberListResponseDto;
import com.checkping.service.Member.dto.response.MemberResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    private MemberRepository memberRepository;
    private OrganizationRepository organizationRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberRepository = mock(MemberRepository.class);
        organizationRepository = mock(OrganizationRepository.class);
        passwordEncoder = new BCryptPasswordEncoder();
        memberService = new MemberService(memberRepository, organizationRepository, passwordEncoder);
    }

    @Test
    void registerMember_Success() {
        // Given
        UUID organizationId = UUID.randomUUID();
        Organization organization = new Organization();
        organization.setId(organizationId);

        MemberRegisterDto dto = new MemberRegisterDto();
        dto.setEmail("test@example.com");
        dto.setPassword("password123");
        dto.setName("Test User");
        dto.setPhoneNum("01012345678");
        dto.setJobRole("Developer");
        dto.setJobTitle("Engineer");
        dto.setOrganizationId(organizationId);
        dto.setType("MEMBER");
        dto.setStatus("ACTIVE");

        when(memberRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(organization));
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        MemberResponseDto result = memberService.registerMember(dto);

        // Then
        assertNotNull(result);
        assertEquals(dto.getEmail(), result.getEmail());
        verify(memberRepository, times(1)).save(any(Member.class));

        // 콘솔 출력
        System.out.println("Register Member Test - Success");
        System.out.println("Result: " + result);
    }

    @Test
    void registerMember_Fail_ExistingEmail() {
        // Given
        String existingEmail = "test@example.com";

        MemberRegisterDto dto = new MemberRegisterDto();
        dto.setEmail(existingEmail);
        dto.setPassword("password123");

        when(memberRepository.existsByEmail(existingEmail)).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> memberService.registerMember(dto));
        assertEquals("이미 가입된 이메일입니다.: " + existingEmail, exception.getMessage());
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void updateMember_Success() {
        // Given
        UUID memberId = UUID.randomUUID();

        Member existingMember = new Member();
        existingMember.setId(memberId);
        existingMember.setName("Old Name");
        existingMember.setPhoneNum("01011112222");
        existingMember.setJobRole("Developer");
        existingMember.setJobTitle("Junior");
        existingMember.setType(Member.Type.MEMBER); // 추가
        existingMember.setStatus(Member.Status.ACTIVE); // 추가
        existingMember.setRemark("Old Remark");

        MemberUpdateDto dto = new MemberUpdateDto();
        dto.setName("New Name");
        dto.setPhoneNum("01012345678");
        dto.setJobRole("Manager");
        dto.setJobTitle("Senior Manager");
        dto.setRemark("Updated Remark");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(existingMember));
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 출력: 업데이트 전 정보
        System.out.println("Before Update:");
        System.out.println("Name: " + existingMember.getName());
        System.out.println("PhoneNum: " + existingMember.getPhoneNum());
        System.out.println("JobRole: " + existingMember.getJobRole());
        System.out.println("JobTitle: " + existingMember.getJobTitle());
        System.out.println("Remark: " + existingMember.getRemark());

        // When
        MemberResponseDto result = memberService.updateMember(memberId, dto);

        // 출력: 업데이트 후 정보
        System.out.println("After Update:");
        System.out.println("Name: " + result.getName());
        System.out.println("PhoneNum: " + result.getPhoneNum());
        System.out.println("JobRole: " + result.getJobRole());
        System.out.println("JobTitle: " + result.getJobTitle());
        System.out.println("Remark: " + result.getRemark());

        // Then
        assertNotNull(result);
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getPhoneNum(), result.getPhoneNum());
        assertEquals(dto.getJobRole(), result.getJobRole());
        verify(memberRepository, times(1)).save(any(Member.class));
    }


    @Test
    void changePassword_Success() {
        // Given
        UUID memberId = UUID.randomUUID();
        String currentPassword = "oldPassword";
        String newPassword = "newPassword123";

        Member existingMember = new Member();
        existingMember.setId(memberId);
        existingMember.setPassword(passwordEncoder.encode(currentPassword));

        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setCurrentPassword(currentPassword);
        dto.setNewPassword(newPassword);
        dto.setConfirmNewPassword(newPassword);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(existingMember));

        // When
        memberService.changePassword(memberId, dto);

        // Then
        assertTrue(passwordEncoder.matches(newPassword, existingMember.getPassword()));
        verify(memberRepository, times(1)).save(existingMember);
    }

    @Test
    void changePassword_Fail_InvalidCurrentPassword() {
        // Given
        UUID memberId = UUID.randomUUID();
        String currentPassword = "wrongPassword";

        Member existingMember = new Member();
        existingMember.setId(memberId);
        existingMember.setPassword(passwordEncoder.encode("actualPassword"));

        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setCurrentPassword(currentPassword);
        dto.setNewPassword("newPassword123");
        dto.setConfirmNewPassword("newPassword123");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(existingMember));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> memberService.changePassword(memberId, dto));
        assertEquals("현재 비밀번호가 일치하지 않습니다.", exception.getMessage());
        verify(memberRepository, never()).save(existingMember);
    }

    @Test
    void getMemberByEmail_Success() {
        // Given
        String email = "test@example.com";

        Member member = new Member();
        member.setId(UUID.randomUUID());
        member.setEmail(email);
        member.setName("Test User");
        member.setPhoneNum("01012345678");
        member.setType(Member.Type.MEMBER);
        member.setStatus(Member.Status.ACTIVE);

        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));

        // When
        MemberResponseDto result = memberService.getMemberByEmail(email);

        // Then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals("Test User", result.getName());
        verify(memberRepository, times(1)).findByEmail(email);
    }

    @Test
    void getMemberByEmail_Fail() {
        // Given
        String email = "notfound@example.com";

        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> memberService.getMemberByEmail(email));
        assertEquals("사용자가 존재하지 않습니다: " + email, exception.getMessage());
        verify(memberRepository, times(1)).findByEmail(email);
    }

    @Test
    void getAllMemberList_Success() {
        // Given
        Organization organization = new Organization();
        organization.setId(UUID.randomUUID());
        organization.setName("Test Organization");

        List<Member> members = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Member member = new Member();
            member.setId(UUID.randomUUID());
            member.setEmail("user" + i + "@example.com");
            member.setName("User " + i);
            member.setPhoneNum("0101234567" + i);
            member.setType(Member.Type.MEMBER);
            member.setStatus(Member.Status.ACTIVE);
            member.setOrganization(organization);
            members.add(member);
        }

        when(memberRepository.findAll()).thenReturn(members);

        // When
        MemberListResponseDto result = memberService.getAllMemberListAsDto();

        // Then
        assertNotNull(result);
        assertEquals(3, result.getMembers().size());
        assertEquals("User 1", result.getMembers().get(0).getName());
        verify(memberRepository, times(1)).findAll();

        // 콘솔 출력
        System.out.println("Get All Member List Test - Success");
        System.out.println("Result: " + result.getMembers());
    }

    @Test
    void getAllMemberList_WithDto_Success() {
        // Given
        Organization organization = new Organization();
        organization.setId(UUID.randomUUID());
        organization.setName("Test Organization");

        List<Member> members = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Member member = new Member();
            member.setId(UUID.randomUUID());
            member.setEmail("user" + i + "@example.com");
            member.setName("User " + i);
            member.setPhoneNum("0101234567" + i);
            member.setType(Member.Type.MEMBER);
            member.setStatus(Member.Status.ACTIVE);
            member.setOrganization(organization);
            members.add(member);
        }

        // Mock `findAll()` 메서드의 반환값
        when(memberRepository.findAll()).thenReturn(members);

        // When
        MemberListResponseDto result = memberService.getAllMemberListAsDto();

        // Then
        assertNotNull(result);
        assertEquals(3, result.getMembers().size()); // 리스트 크기 검증
        assertEquals("User 1", result.getMembers().get(0).getName()); // 첫 번째 멤버 이름 검증
        verify(memberRepository, times(1)).findAll(); // `findAll` 호출 횟수 검증

        // 콘솔 출력
        System.out.println("Get All Member List Test - Success");
        System.out.println("Result: " + result.getMembers());
    }
}
