package com.checkping.service;

import com.checkping.domain.member.Member;
import com.checkping.domain.member.Organization;
import com.checkping.dto.member.request.ChangePasswordDto;
import com.checkping.dto.member.request.MemberRegisterDto;
import com.checkping.dto.member.request.MemberUpdateDto;
import com.checkping.dto.member.response.MemberListResponseDto;
import com.checkping.dto.member.response.MemberResponseDto;
import com.checkping.infra.repository.member.MemberRepository;
import com.checkping.infra.repository.member.OrganizationRepository;
import com.checkping.service.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.ActiveProfiles;  // 필요한 경우 사용

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"profile-name=test"})
// @ActiveProfiles("test") // 테스트 프로필 적용 시
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    private Organization savedOrg;

    @BeforeEach
    void setUp() {
        // 테스트용 Organization 엔티티 생성 후 저장
        Organization org = Organization.builder()
                .name("TestOrganization")
                .type(Organization.Type.DEVELOPER)
                .status(Organization.Status.ACTIVE)
                .build();
        savedOrg = organizationRepository.save(org);
    }

    @Test
    @DisplayName("1) 회원 등록 성공 케이스")
    void registerMemberSuccessTest() {
        // given
        MemberRegisterDto dto = new MemberRegisterDto();
        dto.setOrganizationId(savedOrg.getId());
        dto.setEmail("register@test.com");
        dto.setPassword("rawPw123!");
        dto.setName("테스트 유저");
        dto.setRole("MEMBER");              // 필수: role
        dto.setPhoneNum("010-1234-5678");   // 필수: phoneNum
        dto.setJobRole("개발자");           // 예시로 모두 세팅
        dto.setJobTitle("주임");
        dto.setIntroduction("안녕하세요");
        dto.setRemark("비고");

        // when
        MemberResponseDto response = memberService.registerMember(dto);

        // then
        assertNotNull(response.getId(), "회원 가입 후에는 ID가 존재해야 함");
        assertEquals("register@test.com", response.getEmail());
        assertEquals("테스트 유저", response.getName());

        // DB에서 실제로 저장됐는지 재확인
        Member memberInDb = memberRepository.findById(response.getId())
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        assertEquals(savedOrg.getId(), memberInDb.getOrganization().getId());
        assertNotEquals("rawPw123!", memberInDb.getPassword(),
                "DB에는 BCrypt 등으로 암호화된 비밀번호가 저장돼야 함");
    }

    @Test
    @DisplayName("2) 회원 등록 실패: 이미 가입된 이메일")
    void registerMemberFailAlreadyExistsTest() {
        // given - 먼저 하나 등록
        MemberRegisterDto dto = new MemberRegisterDto();
        dto.setOrganizationId(savedOrg.getId());
        dto.setEmail("duplicate@test.com");
        dto.setPassword("password");
        dto.setRole("MEMBER");
        dto.setPhoneNum("010-1234-5678");
        dto.setName("중복테스트");
        dto.setJobRole("개발");
        dto.setJobTitle("사원");
        dto.setIntroduction("중복 테스트 소개");
        dto.setRemark("비고");
        memberService.registerMember(dto);

        // when - 같은 이메일로 다시 등록 시도
        MemberRegisterDto dto2 = new MemberRegisterDto();
        dto2.setOrganizationId(savedOrg.getId());
        dto2.setEmail("duplicate@test.com"); // 같은 이메일
        dto2.setPassword("otherPassword");
        dto2.setRole("ADMIN");
        dto2.setPhoneNum("010-4321-9999");
        dto2.setName("다른사람");
        dto2.setJobRole("마케팅");
        dto2.setJobTitle("대리");
        dto2.setIntroduction("중복 실패 테스트");
        dto2.setRemark("비고2");

        // then
        assertThrows(IllegalArgumentException.class, () ->
                        memberService.registerMember(dto2),
                "이미 가입된 이메일로 회원가입 시도하면 예외 발생"
        );
    }

    @Test
    @DisplayName("3) 회원 정보 수정 테스트")
    void updateMemberTest() {
        // given - 회원을 먼저 하나 등록
        MemberRegisterDto dto = new MemberRegisterDto();
        dto.setOrganizationId(savedOrg.getId());
        dto.setEmail("updateme@test.com");
        dto.setPassword("pw");
        dto.setRole("MEMBER");
        dto.setName("수정전 이름");
        dto.setPhoneNum("010-0000-0000");
        dto.setJobRole("개발");
        dto.setJobTitle("사원");
        dto.setIntroduction("수정 전");
        dto.setRemark("비고 전");
        MemberResponseDto registered = memberService.registerMember(dto);

        // 수정용 DTO 준비
        MemberUpdateDto updateDto = new MemberUpdateDto();
        updateDto.setName("수정된 이름");
        updateDto.setPhoneNum("010-9999-9999");
        updateDto.setJobRole("수정된 직무");
        updateDto.setJobTitle("수정된 직급");
        updateDto.setIntroduction("소개 변경");
        updateDto.setRemark("비고 변경");

        // when
        MemberResponseDto updatedResponse = memberService.updateMember(registered.getId(), updateDto);

        // then
        assertEquals("수정된 이름", updatedResponse.getName());
        assertEquals("010-9999-9999", updatedResponse.getPhoneNum());
        assertEquals("수정된 직무", updatedResponse.getJobRole());
        assertEquals("수정된 직급", updatedResponse.getJobTitle());
    }

    @Test
    @DisplayName("4) 회원 비밀번호 변경 - 정상 케이스")
    void changePasswordSuccessTest() {
        // given - 회원 등록
        MemberRegisterDto registerDto = new MemberRegisterDto();
        registerDto.setOrganizationId(savedOrg.getId());
        registerDto.setEmail("pwchange@test.com");
        registerDto.setRole("ADMIN");
        registerDto.setPhoneNum("010-1234-5678");
        registerDto.setPassword("oldPw123");
        registerDto.setName("비번변경테스트");
        registerDto.setJobRole("개발");
        registerDto.setJobTitle("대리");
        registerDto.setIntroduction("비번체인지 소개");
        registerDto.setRemark("비고");
        MemberResponseDto registered = memberService.registerMember(registerDto);

        // 실제 DB에 있는 엔티티 가져오기
        Member member = memberRepository.findById(registered.getId())
                .orElseThrow();

        // 비밀번호 변경용 DTO
        ChangePasswordDto cpDto = new ChangePasswordDto();
        cpDto.setCurrentPassword("oldPw123");
        cpDto.setNewPassword("newPw456");
        cpDto.setConfirmNewPassword("newPw456");

        // when
        memberService.changePassword(member.getId(), cpDto);

        // then
        Member updatedMember = memberRepository.findById(member.getId())
                .orElseThrow();
        assertNotEquals("oldPw123", updatedMember.getPassword(), "구 비번과 달라야 함");
        assertNotNull(updatedMember.getPwChangeAt(), "비밀번호 변경 시각이 기록돼야 함");
    }

    @Test
    @DisplayName("5) 회원 비밀번호 변경 - 현재 비밀번호 불일치 시 실패")
    void changePasswordFailWrongCurrent() {
        // given
        MemberRegisterDto registerDto = new MemberRegisterDto();
        registerDto.setOrganizationId(savedOrg.getId());
        registerDto.setEmail("wrongcurrent@test.com");
        registerDto.setRole("ADMIN");
        registerDto.setPhoneNum("010-1234-5678");
        registerDto.setPassword("oldPw999");
        registerDto.setName("비밀번호불일치테스트");
        registerDto.setJobRole("개발");
        registerDto.setJobTitle("사원");
        registerDto.setIntroduction("비번 불일치 소개");
        registerDto.setRemark("비고");
        MemberResponseDto registered = memberService.registerMember(registerDto);

        ChangePasswordDto cpDto = new ChangePasswordDto();
        cpDto.setCurrentPassword("wrongOldPw"); // 실제랑 다름
        cpDto.setNewPassword("newPw");
        cpDto.setConfirmNewPassword("newPw");

        // when & then
        assertThrows(IllegalArgumentException.class, () ->
                        memberService.changePassword(registered.getId(), cpDto),
                "현재 비밀번호 불일치 시 예외 발생"
        );
    }

    @Test
    @DisplayName("6) 회원 비밀번호 변경 - 새 비밀번호 != 확인 비밀번호")
    void changePasswordFailMismatchNewAndConfirm() {
        // given
        MemberRegisterDto registerDto = new MemberRegisterDto();
        registerDto.setOrganizationId(savedOrg.getId());
        registerDto.setEmail("pwMismatch@test.com");
        registerDto.setPassword("oldPw");
        registerDto.setRole("ADMIN");
        registerDto.setPhoneNum("010-1234-5678");
        registerDto.setName("비번불일치테스터");
        registerDto.setJobRole("개발");
        registerDto.setJobTitle("사원");
        registerDto.setIntroduction("비번 불일치 테스트");
        registerDto.setRemark("비고");
        MemberResponseDto registered = memberService.registerMember(registerDto);

        ChangePasswordDto cpDto = new ChangePasswordDto();
        cpDto.setCurrentPassword("oldPw");
        cpDto.setNewPassword("abc123");
        cpDto.setConfirmNewPassword("xyz999"); // 서로 다름

        // when & then
        assertThrows(IllegalArgumentException.class, () ->
                        memberService.changePassword(registered.getId(), cpDto),
                "새 비밀번호 != 확인 비밀번호 시 예외 발생"
        );
    }

    @Test
    @DisplayName("7) 이메일로 회원 조회")
    void getMemberByEmailTest() {
        // given
        String testEmail = "emailLookup@test.com";
        MemberRegisterDto registerDto = new MemberRegisterDto();
        registerDto.setOrganizationId(savedOrg.getId());
        registerDto.setEmail(testEmail);
        registerDto.setRole("ADMIN");
        registerDto.setPhoneNum("010-1234-5678");
        registerDto.setPassword("somePw");
        registerDto.setName("이메일조회테스터");
        registerDto.setJobRole("개발");
        registerDto.setJobTitle("사원");
        registerDto.setIntroduction("이메일 조회 소개");
        registerDto.setRemark("비고");
        memberService.registerMember(registerDto);

        // when
        MemberResponseDto responseDto = memberService.getMemberByEmail(testEmail);

        // then
        assertEquals(testEmail, responseDto.getEmail());
        assertEquals("이메일조회테스터", responseDto.getName());
    }

    @Test
    @DisplayName("8) 전체 회원 목록 조회")
    void getAllMemberListAsDtoTest() {
        // given - 회원 여러 명 등록
        MemberRegisterDto dto1 = new MemberRegisterDto();
        dto1.setOrganizationId(savedOrg.getId());
        dto1.setEmail("list1@test.com");
        dto1.setPassword("pw1");
        dto1.setRole("ADMIN");
        dto1.setPhoneNum("010-1234-5678");
        dto1.setName("목록1");
        dto1.setJobRole("개발");
        dto1.setJobTitle("사원");
        dto1.setIntroduction("소개1");
        dto1.setRemark("비고1");
        memberService.registerMember(dto1);

        MemberRegisterDto dto2 = new MemberRegisterDto();
        dto2.setOrganizationId(savedOrg.getId());
        dto2.setEmail("list2@test.com");
        dto2.setPassword("pw2");
        dto2.setRole("ADMIN");
        dto2.setPhoneNum("010-1234-5678");
        dto2.setName("목록2");
        dto2.setJobRole("개발");
        dto2.setJobTitle("사원");
        dto2.setIntroduction("소개2");
        dto2.setRemark("비고2");
        memberService.registerMember(dto2);

        // when
        MemberListResponseDto listResponse = memberService.getAllMemberListAsDto();

        // then
        List<MemberResponseDto> members = listResponse.getMembers();
        assertTrue(members.size() >= 2, "최소 2명 이상의 회원이 존재해야 함");
        // 목록에서 방금 등록한 이메일이 보이는지 확인
        boolean found1 = members.stream().anyMatch(m -> "list1@test.com".equals(m.getEmail()));
        boolean found2 = members.stream().anyMatch(m -> "list2@test.com".equals(m.getEmail()));
        assertTrue(found1, "list1@test.com 회원이 목록에 있어야 함");
        assertTrue(found2, "list2@test.com 회원이 목록에 있어야 함");
    }
}