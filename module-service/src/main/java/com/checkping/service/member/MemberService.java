package com.checkping.service.member;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
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

// TODO BaseException 을 상속하는 커스텀 Exception 작성하기

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
    public MemberResponseDto getMemberById(UUID memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException("회원이 존재하지 않습니다: " + memberId, ErrorCode.USER_NOT_FOUND));
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
            throw new BaseException("이미 사용중인 이메일입니다.", ErrorCode.DUPLICATE_EMAIL);
        }

        // 조직(Organization) 존재 여부 확인
        Organization organization = organizationRepository.findById(dto.getOrganizationId())
                .orElseThrow(() -> new BaseException("조직이 존재하지 않습니다: " + dto.getOrganizationId(), ErrorCode.USER_NOT_FOUND));

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
                .orElseThrow(() -> new BaseException("회원이 존재하지 않습니다: " + memberId, ErrorCode.USER_NOT_FOUND));

        // DTO -> 엔티티 업데이트
        MemberUpdateDto.toEntity(existingMember, dto);

        // 수정된 엔티티 저장
        Member updatedMember = memberRepository.save(existingMember);

        // DTO로 변환하여 반환
        return MemberResponseDto.fromEntity(updatedMember);
    }

    // 비밀번호 변경
    public void changePassword(UUID memberId, ChangePasswordDto dto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException("회원이 존재하지 않습니다: " + memberId, ErrorCode.USER_NOT_FOUND));

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(dto.getCurrentPassword(), member.getPassword())) {
            throw new BaseException("현재 비밀번호가 일치하지 않습니다.", ErrorCode.INVALID_LOGIN_CREDENTIALS);
        }

        // 새 비밀번호와 확인 비밀번호 일치 여부 확인
        if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            throw new BaseException("새 비밀번호와 확인 비밀번호가 일치하지 않습니다.", ErrorCode.INVALID_LOGIN_CREDENTIALS);
        }

        // 비밀번호 암호화 적용
        String encodedPassword = passwordEncoder.encode(dto.getNewPassword());

        // 엔티티에 비밀번호 변경 로직 적용
        ChangePasswordDto.updatePassword(member, encodedPassword);

        // 수정된 엔티티 저장
        memberRepository.save(member);
    }

    // 회원 삭제
    // TODO 회원 삭제 되면 로그인 안되도록 코드 수정하기
    public void deleteMember(UUID memberId, String reasonForDelete) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException("회원이 존재하지 않습니다: " + memberId, ErrorCode.USER_NOT_FOUND));
        if (!member.isActive()) {
            throw new BaseException("이미 삭제된 회원입니다.", ErrorCode.ALREADY_APPLIED);
        }
        // 회원 삭제 처리
        member.deleteAccount(reasonForDelete);
        memberRepository.save(member);
    }
}