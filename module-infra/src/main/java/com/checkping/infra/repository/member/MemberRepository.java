package com.checkping.infra.repository.member;

import com.checkping.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 이메일로 회원 조회
    Optional<Member> findByEmail(String email);

    // 특정 이메일 존재 여부
    boolean existsByEmail(String email);

    // 이메일 + 상태로 회원 조회
    Optional<Member> findByEmailAndStatus(String email, Member.Status status);

    Page<Member> findAll(Pageable pageable); // 페이징 지원

    // 소속 업체 아이디로 회원 조회
    Page<Member> findByOrganizationId(Long organizationId, Pageable pageable);
}