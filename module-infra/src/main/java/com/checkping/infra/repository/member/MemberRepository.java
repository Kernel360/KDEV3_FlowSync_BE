package com.checkping.infra.repository.member;

import com.checkping.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {
    // 이메일로 회원 조회
    Optional<Member> findByEmail(String email);

    // 특정 이메일 존재 여부
    boolean existsByEmail(String email);

    // 이메일 + 상태로 회원 조회
    Optional<Member> findByEmailAndStatus(String email, Member.Status status);
}