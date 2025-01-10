package com.checkping.infra.member;

import com.checkping.domain.member.Member;
import com.checkping.domain.member.Organization;
import com.checkping.infra.repository.member.MemberRepository;
import com.checkping.infra.repository.member.OrganizationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
// import org.springframework.test.context.ActiveProfiles; // 필요하다면 프로필 지정

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
// @ActiveProfiles("test") // test용 프로필이 있다면
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Test
    @DisplayName("MemberRepository save & findByEmail 테스트")
    void saveAndFindByEmail() {
        // given
        Organization org = Organization.builder()
                .name("Test Organization")
                .type(Organization.Type.DEVELOPER)
                .status(Organization.Status.ACTIVE)
                .regAt(LocalDateTime.now())
                .build();
        Organization savedOrg = organizationRepository.save(org);

        Member member = Member.builder()
                .email("test@example.com")
                .password("encoded-pw")
                .phoneNum(("010-1234-5678"))
                .name("테스트회원")
                .role(Member.Role.MEMBER)
                .regAt(LocalDateTime.now())
                .organization(savedOrg)
                .status(Member.Status.ACTIVE)
                .build();

        // when
        Member savedMember = memberRepository.save(member);

        // then
        Optional<Member> optionalMember = memberRepository.findByEmail("test@example.com");
        assertTrue(optionalMember.isPresent());
        assertEquals("test@example.com", optionalMember.get().getEmail());
        assertEquals(savedOrg.getId(), optionalMember.get().getOrganization().getId());
    }

    @Test
    @DisplayName("existsByEmail 테스트")
    void existsByEmail() {
        // given
        Member member = Member.builder()
                .email("another@example.com")
                .password("pw")
                .name("또다른회원")
                .role(Member.Role.MEMBER)
                .regAt(LocalDateTime.now())
                .phoneNum(("010-1234-5678"))
                .status(Member.Status.ACTIVE)
                .organization(
                        organizationRepository.save(
                                Organization.builder()
                                        .name("Org2")
                                        .regAt(LocalDateTime.now())
                                        .type(Organization.Type.CUSTOMER)
                                        .status(Organization.Status.ACTIVE)
                                        .build()
                        )
                )
                .build();

        memberRepository.save(member);

        // when
        boolean result = memberRepository.existsByEmail("another@example.com");

        // then
        assertTrue(result);
    }
}
