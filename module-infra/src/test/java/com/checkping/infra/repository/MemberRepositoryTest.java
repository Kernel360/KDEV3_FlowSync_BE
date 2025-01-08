package com.checkping.infra.repository;

import com.checkping.domain.member.Member;
import com.checkping.domain.member.Organization;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Test
    void testInsertMember() {
        Organization organization = Organization.builder()
                .type(Organization.Type.CUSTOMER)
                .status(Organization.Status.ACTIVE)
                .regAt(LocalDateTime.now())
                .brNumber("12345")
                .name("kernelPark")
                .build();

        organizationRepository.save(organization);

        for (int i = 0; i < 10; i++) {
            Member member = Member.builder()
                    .email("aaa" + i + "@email.com")
                    .organization(organization)
                    .type(Member.Type.ADMIN)
                    .regAt(LocalDateTime.now())
                    .status(Member.Status.ACTIVE)
                    .password("1234")
                    .name("Member" + i)
                    .phoneNum("12345")
                    .loginFailCount(0)
                    .isLoginLockedYn('N')
                    .lastLoginAt(LocalDateTime.now())
                    .build();

            memberRepository.save(member);
        }

        memberRepository.findAll().forEach(System.out::println);
    }

    @Test
    void testFindByEmail() {
        // GIVEN: Insert a new Member for testing
        Organization organization = Organization.builder()
                .type(Organization.Type.CUSTOMER)
                .status(Organization.Status.ACTIVE)
                .regAt(LocalDateTime.now())
                .brNumber("67890")
                .name("TestOrganization")
                .build();

        organizationRepository.save(organization);

        String testEmail = "testUser@email.com";
        Member testMember = Member.builder()
                .email(testEmail)
                .organization(organization)
                .type(Member.Type.MEMBER)
                .regAt(LocalDateTime.now())
                .status(Member.Status.ACTIVE)
                .password("password")
                .name("TestUser")
                .phoneNum("67890")
                .loginFailCount(0)
                .isLoginLockedYn('N')
                .lastLoginAt(LocalDateTime.now())
                .build();

        memberRepository.save(testMember);

        // WHEN: Find the Member by email
        Optional<Member> foundMember = memberRepository.findByEmail(testEmail);

        // THEN: Assert the Member is found and has the correct data
        assertTrue(foundMember.isPresent());
        assertEquals(testEmail, foundMember.get().getEmail());
        assertEquals("TestUser", foundMember.get().getName());
    }

    @Test
    void testFindAll() {
        // GIVEN: Ensure there are some members in the repository
        List<Member> allMembers = memberRepository.findAll();

        // WHEN: Retrieve all members
        List<Member> retrievedMembers = memberRepository.findAll();

        // THEN: Assert that the size of the retrieved list matches the expected size
        assertNotNull(retrievedMembers);
        assertEquals(allMembers.size(), retrievedMembers.size());

        // Print all members for verification
        retrievedMembers.forEach(System.out::println);
    }
}
