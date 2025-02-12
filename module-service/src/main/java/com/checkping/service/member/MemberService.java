package com.checkping.service.member;

import com.checkping.common.dto.PageInfo;
import com.checkping.common.dto.PageMetaResponse;
import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import com.checkping.common.utils.FileResponse;
import com.checkping.domain.member.Member;
import com.checkping.domain.member.Organization;
import com.checkping.domain.member.projection.ProjectList;
import com.checkping.domain.project.Project;
import com.checkping.dto.ProjectListGet;
import com.checkping.dto.member.request.ChangePasswordDto;
import com.checkping.dto.member.request.MemberRegisterDto;
import com.checkping.dto.member.request.MemberUpdateDto;
import com.checkping.dto.member.response.MemberListResponseDto;
import com.checkping.dto.member.response.MemberResponseDto;
import com.checkping.dto.member.response.MemberSignatureExistResponseDto;
import com.checkping.dto.member.response.MemberSignatureResponseDto;
import com.checkping.exception.member.InvalidInputValueException;
import com.checkping.infra.repository.member.MemberRepository;
import com.checkping.infra.repository.member.OrganizationRepository;
import com.checkping.service.member.auth.RedisConnectionCheckService;
import com.checkping.infra.repository.member.ProjectQueryRepository;
import com.checkping.service.member.util.CurrentMemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// TODO BaseException 을 상속하는 커스텀 Exception 작성하기

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;          // 도메인 인터페이스
    private final OrganizationRepository organizationRepository; // 조직 레포지토리(예: JPA)
    private final BCryptPasswordEncoder passwordEncoder;
    private final CurrentMemberUtil currentMemberUtil;
    private final StringRedisTemplate redisTemplateForInactiveMemers;
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisConnectionCheckService redisConnectionCheckService;
    private final ProjectQueryRepository projectQueryRepository;

    // 아이디로 회원 조회
    public MemberResponseDto getMemberById(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(
                () -> new BaseException("회원이 존재하지 않습니다: " + memberId, ErrorCode.USER_NOT_FOUND));
        return MemberResponseDto.fromEntity(member);
    }

    // 페이징된 전체 회원 목록 조회
    public MemberListResponseDto getAllMembersWithFilters(
        int page, int size, String roleParam, String statusParam, String keyword
    ) {
        // (1) 페이지, 사이즈 유효성 검증
        if (page < 0 || size < 1) {
            throw new InvalidInputValueException("유효하지 않은 페이지/사이즈 값입니다.");
        }

        // (2) 문자열로 들어온 role, status를 Enum으로 변환
        Member.Role role = null;
        if (roleParam != null && !roleParam.isBlank()) {
            try {
                role = Member.Role.valueOf(roleParam.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidInputValueException("유효하지 않은 role 값입니다. " + roleParam);
            }
        }

        Member.Status status = null;
        if (statusParam != null && !statusParam.isBlank()) {
            try {
                status = Member.Status.valueOf(statusParam.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidInputValueException("유효하지 않은 status 값입니다. " + statusParam);
            }
        }

        // (3) 검색어 null 처리
        if (keyword != null && keyword.isBlank()) {
            keyword = null;
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Member> memberPage = memberRepository.findAllWithFilters(role, status, keyword,
            pageable);

        // (4) page 범위 초과 시 예외 처리
        if (page >= memberPage.getTotalPages() && memberPage.getTotalPages() != 0) {
            throw new InvalidInputValueException("페이지 번호가 범위를 벗어났습니다.");
        }

        // (5) 결과 DTO 변환
        return MemberListResponseDto.fromEntityPage(memberPage);
    }

    // 회원 등록
    public MemberResponseDto registerMember(MemberRegisterDto dto) {
        // 이메일 중복 체크
        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new BaseException("이미 사용중인 이메일입니다.", ErrorCode.DUPLICATE_EMAIL);
        }

        // 조직(Organization) 존재 여부 확인
        Organization organization = organizationRepository.findById(dto.getOrganizationId())
            .orElseThrow(() -> new BaseException("조직이 존재하지 않습니다: " + dto.getOrganizationId(),
                ErrorCode.USER_NOT_FOUND));

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
    public MemberResponseDto updateMember(Long memberId, MemberUpdateDto dto) {
        // 기존 회원 찾기
        Member existingMember = memberRepository.findById(memberId)
            .orElseThrow(
                () -> new BaseException("회원이 존재하지 않습니다: " + memberId, ErrorCode.USER_NOT_FOUND));

        // DTO -> 엔티티 업데이트
        MemberUpdateDto.toEntity(existingMember, dto);

        // 수정된 엔티티 저장
        Member updatedMember = memberRepository.save(existingMember);

        // DTO로 변환하여 반환
        return MemberResponseDto.fromEntity(updatedMember);
    }

    // 비밀번호 변경
    public void changePassword(Long memberId, ChangePasswordDto dto) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(
                () -> new BaseException("회원이 존재하지 않습니다: " + memberId, ErrorCode.USER_NOT_FOUND));

        // TODO 관리자가 비밀번호 변경하므로 현재 비밀번호 확인 일단 보류
        // 현재 비밀번호 확인
//        if (!passwordEncoder.matches(dto.getCurrentPassword(), member.getPassword())) {
//            throw new BaseException("현재 비밀번호가 일치하지 않습니다.", ErrorCode.INVALID_LOGIN_CREDENTIALS);
//        }

        // 새 비밀번호와 확인 비밀번호 일치 여부 확인
        if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            throw new BaseException("새 비밀번호와 확인 비밀번호가 일치하지 않습니다.",
                ErrorCode.INVALID_LOGIN_CREDENTIALS);
        }

        // 비밀번호 암호화 적용
        String encodedPassword = passwordEncoder.encode(dto.getNewPassword());

        // 엔티티에 비밀번호 변경 로직 적용
        ChangePasswordDto.updatePassword(member, encodedPassword);

        // 수정된 엔티티 저장
        memberRepository.save(member);
    }

    // 회원 삭제
    public void deleteMember(Long memberId, String reasonForDelete) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(
                () -> new BaseException("회원이 존재하지 않습니다: " + memberId, ErrorCode.USER_NOT_FOUND));
        if (member.isDeleted()) {
            throw new BaseException("이미 삭제된 회원입니다.", ErrorCode.ALREADY_APPLIED);
        }
        // 회원 삭제 처리
        member.deleteAccount(reasonForDelete);
        memberRepository.save(member);
    }

    //업체별 회원 목록 조회
    public MemberListResponseDto getMembersByOrganizationId(Long organizationId, int page,
        int size) {

        //존재하지 않는 업체 아이디인 경우 예외 처리
        if (!organizationRepository.existsById(organizationId)) {
            throw new BaseException("해당 업체가 존재하지 않습니다.", ErrorCode.ORGANIZATION_NOT_FOUND);
        }

        //페이지에 음수들어온 경우 예외 처리
        if (page < 0 || size < 0) {
            throw new InvalidInputValueException("페이지 번호는 0보다 크고 사이즈는 1보다 커야합니다.");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Member> memberPage = memberRepository.findByOrganizationId(organizationId, pageable);

        //범위 바깥의 페이지 요청
        if (page >= memberPage.getTotalPages() && memberPage.getTotalPages() != 0) {
            throw new InvalidInputValueException("페이지 번호가 범위를 벗어났습니다.");
        }
        //페이지에 회원이 없는 경우 예외 처리
        if (memberPage.isEmpty()) {
            throw new BaseException("해당 업체에 회원이 존재하지 않습니다.", ErrorCode.USER_NOT_FOUND);
        }
        // MemberListResponseDto로 변환
        return MemberListResponseDto.fromEntityPage(memberPage);
    }

    /**
     * 회원 서명 파일 업로드
     *
     * @param signatureFile 서명 파일
     * @return MemberSignatureResponseDto
     */
    @Transactional
    public MemberSignatureResponseDto uploadSignature(FileResponse signatureFile) {
        // 회원 조회
        Member member = currentMemberUtil.getCurrentMember();

        // 서명 파일 URL 저장
        member.uploadSignature(signatureFile.url());

        // 결과 DTO 반환
        return MemberSignatureResponseDto.toDto(member);
    }

    /**
     * 회원 서명 파일 존재 여부
     *
     * @return MemberSignatureExistResponseDto
     */
    public MemberSignatureExistResponseDto getSignature() {

        Member member = currentMemberUtil.getCurrentMember();

        return MemberSignatureExistResponseDto.toDto(member);
    }

    //회원 활성화
    public void activateMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(
                () -> new BaseException("회원이 존재하지 않습니다: " + memberId, ErrorCode.USER_NOT_FOUND));

        if(member.isActive()){
            throw new BaseException("이미 활성화된 회원입니다.", ErrorCode.ALREADY_APPLIED);
        }
        member.activateAccount();
        memberRepository.save(member);

        // Redis 1번 저장소에 비활성화된 회원 ID 삭제
        if(redisConnectionCheckService.isRedisAvailable()){
            redisTemplateForInactiveMemers.delete("inactive:member:" + memberId);
        }
    }

    /*
    * 회원 비활성화
    * 관리자가 회원을 비활성화 처리합니다. - inactiveAccount
    * */

    public void deactivateMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(
                () -> new BaseException("회원이 존재하지 않습니다: " + memberId, ErrorCode.USER_NOT_FOUND));

        if(member.isDeleted()){
            throw new BaseException("삭제된 회원입니다.", ErrorCode.ALREADY_APPLIED);
        }

        if(!member.isActive()){
            throw new BaseException("이미 비활성화된 회원입니다.", ErrorCode.ALREADY_APPLIED);
        }
        member.deactivateAccount();
        memberRepository.save(member);

        // Redis 1번 저장소에 비활성화된 회원 ID 저장
        if(redisConnectionCheckService.isRedisAvailable()){
            redisTemplateForInactiveMemers.opsForValue().set("inactive:member:" + memberId, "true");
        }

    }

    public String getMemberStatus(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(
                () -> new BaseException("회원이 존재하지 않습니다: " + memberId, ErrorCode.USER_NOT_FOUND));
        return member.getStatus().name();
    }

    public PageInfo.Response<ProjectListGet.Response> getProjectsByMember(Long memberId, String managementStep, PageInfo.Request pageRequest) {

        Pageable pageable = PageRequest.of(
                pageRequest.getCurrentPage() - 1,
                pageRequest.getPageSize());

        Project.ManagementStep validManagementStep = checkManagementStep(managementStep);

        Page<ProjectList> result = projectQueryRepository.getProjectsByMemberAndOrganization(
                null,
                memberId,
                validManagementStep,
                pageRequest.getKeyword(),
                pageable);

        List<ProjectListGet.Response> dtoList = result.getContent().stream().map(ProjectListGet.Response::toDto).toList();
        PageMetaResponse meta = PageMetaResponse.fromPage(result);

        return PageInfo.Response.<ProjectListGet.Response>builder()
                .dtoList(dtoList)
                .meta(meta.toMap())
                .build();
    }

    private Project.ManagementStep checkManagementStep(String managementStep) {
        if (managementStep == null || managementStep.trim().isEmpty()) {
            return null;
        }
        return Project.ManagementStep.valueOf(managementStep.toUpperCase());
    }
}
