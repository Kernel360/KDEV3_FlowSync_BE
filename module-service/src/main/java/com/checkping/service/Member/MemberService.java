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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final OrganizationRepository organizationRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, OrganizationRepository organizationRepository, BCryptPasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.organizationRepository = organizationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public MemberResponseDto getMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다: " + email));
        return MemberResponseDto.fromEntity(member);
    }

    public MemberListResponseDto getAllMemberListAsDto() {
        List<Member> members = memberRepository.findAll();
        return MemberListResponseDto.fromEntityList(members);
    }

    //회원 등록
    public MemberResponseDto registerMember(MemberRegisterDto dto) {
        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.: " + dto.getEmail());
        }

        Organization organization = organizationRepository.findById(dto.getOrganizationId())
                .orElseThrow(() -> new IllegalArgumentException("업체 id가 존재하지 않습니다.: " + dto.getOrganizationId()));

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        Member member = MemberRegisterDto.toEntity(dto, organization, encodedPassword);

        Member savedMember = memberRepository.save(member);
        return MemberResponseDto.fromEntity(savedMember);
    }

    // 회원 정보 수정
    public MemberResponseDto updateMember(UUID memberId, MemberUpdateDto dto) {
        Member existingMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다: " + memberId));

        MemberUpdateDto.toEntity(existingMember, dto);

        Member updatedMember = memberRepository.save(existingMember);

        return MemberResponseDto.fromEntity(updatedMember); // DTO로 변환 후 반환
    }

    // 비밀번호 변경
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

        // 비밀번호 암호화 및 적용
        String encodedPassword = passwordEncoder.encode(dto.getNewPassword());
        ChangePasswordDto.updatePassword(member, encodedPassword);

        // 수정된 엔티티 저장
        memberRepository.save(member);
    }
}