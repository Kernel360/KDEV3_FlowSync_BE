package com.checkping.infra.repository.member;

import com.checkping.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    // 이름 + 이메일로 회원 조회
    //role, status 필터링 및 검색(이름, 이메일) 적용 쿼리
    // - role이 null이면 무시
    // - status가 null이면 무시
    // - keyword가 공백이거나 null이면 무시
    @Query("""
        SELECT m
        FROM Member m
        WHERE (:role IS NULL OR m.role = :role)
          AND (:status IS NULL OR m.status = :status)
          AND (
              COALESCE(:keyword, '') = ''
              OR LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
              OR LOWER(m.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
          )
    """)
    Page<Member> findAllWithFilters(Member.Role role,
                                    Member.Status status,
                                    String keyword,
                                    Pageable pageable);

    // 소속 업체 아이디로 회원 조회
    Page<Member> findByOrganizationId(Long organizationId, Pageable pageable);
}