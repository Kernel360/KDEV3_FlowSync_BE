package com.checkping.service.member;

import com.checkping.domain.member.Member;
import com.checkping.domain.member.Organization;
import com.checkping.dto.member.request.ChangePasswordDto;
import com.checkping.dto.member.request.MemberRegisterDto;
import com.checkping.dto.member.request.MemberUpdateDto;
import com.checkping.dto.member.response.MemberListResponseDto;
import com.checkping.dto.member.response.MemberResponseDto;
import com.checkping.infra.repository.member.MemberRepository;
import com.checkping.infra.repository.member.OrganizationRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MemberService {

    private final MemberRepository memberRepository;          // 도메인 인터페이스
    private final OrganizationRepository organizationRepository; // 조직 레포지토리(예: JPA)
    private final BCryptPasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, OrganizationRepository organizationRepository, BCryptPasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.organizationRepository = organizationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 이메일로 회원 조회
    public MemberResponseDto getMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다: " + email));
        return MemberResponseDto.fromEntity(member);
    }

    //모든 회원 목록 조회
    public MemberListResponseDto getAllMemberListAsDto() {
        List<Member> members = memberRepository.findAll();
        return MemberListResponseDto.fromEntityList(members);
    }

    // 회원 등록
    public MemberResponseDto registerMember(MemberRegisterDto dto) {
        // 이메일 중복 체크
        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.: " + dto.getEmail());
        }

        // 조직(Organization) 존재 여부 확인
        Organization organization = organizationRepository.findById(dto.getOrganizationId())
                .orElseThrow(() -> new IllegalArgumentException("업체 id가 존재하지 않습니다.: " + dto.getOrganizationId()));

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // DTO -> Member 엔티티 변환
        Member member = MemberRegisterDto.toEntity(dto, organization, encodedPassword);

        // DB 저장
        Member savedMember = memberRepository.save(member);

        // 결과 DTO 반환
        return MemberResponseDto.fromEntity(savedMember);
    }

    // 회원 정보 수정
    public MemberResponseDto updateMember(UUID memberId, MemberUpdateDto dto) {
        // 기존 회원 찾기
        Member existingMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다: " + memberId));

        // DTO -> 엔티티 업데이트
        MemberUpdateDto.toEntity(existingMember, dto);

        // 수정된 엔티티 저장
        Member updatedMember = memberRepository.save(existingMember);

        // DTO로 변환하여 반환
        return MemberResponseDto.fromEntity(updatedMember);
    }

    // 비밀번호 변경, TODO: 한글이 꺠져서 예외 메시지 영어로 변경함, 한글 깨지는 문제 해결
    public void changePassword(UUID memberId, ChangePasswordDto dto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다: " + memberId));

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(dto.getCurrentPassword(), member.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호와 확인 비밀번호 일치 여부 확인
        if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            throw new IllegalArgumentException("새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 암호화 적용
        String encodedPassword = passwordEncoder.encode(dto.getNewPassword());

        // 엔티티에 비밀번호 변경 로직 적용
        ChangePasswordDto.updatePassword(member, encodedPassword);

        // 수정된 엔티티 저장
        memberRepository.save(member);
    }
}