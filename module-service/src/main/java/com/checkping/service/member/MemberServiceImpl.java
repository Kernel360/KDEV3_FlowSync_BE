package com.checkping.service.member;

import com.checkping.common.dto.PageInfo;
import com.checkping.common.dto.PageMetaResponse;
import com.checkping.common.enums.ErrorCode;
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
import com.checkping.exception.member.*;
import com.checkping.infra.repository.member.MemberRepository;
import com.checkping.infra.repository.member.OrganizationRepository;
import com.checkping.infra.repository.member.ProjectQueryRepository;
import com.checkping.service.member.auth.RedisConnectionCheckService;
import com.checkping.service.member.util.CurrentMemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;          // 도메인 인터페이스
    private final OrganizationRepository organizationRepository; // 조직 레포지토리(예: JPA)
    private final BCryptPasswordEncoder passwordEncoder;
    private final CurrentMemberUtil currentMemberUtil;
    private final ProjectQueryRepository projectQueryRepository;
    private final RedisConnectionCheckService redisConnectionCheckService;
    private final StringRedisTemplate redisTemplateForInactiveMemers;


    // 아이디로 회원 조회
    @Override
    public MemberResponseDto getMemberById(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(
                () -> new MemberNotFoundException("회원이 존재하지 않습니다. id: " + memberId));
        return MemberResponseDto.fromEntity(member);
    }

    @Override
    public MemberListResponseDto getAllMembersWithFilters(
            int page,
            int size,
            String roleParam,
            String statusParam,
            String keyword,
            String sortField,
            String sortDirection
    ) {
        // (1) 페이지 및 사이즈 검증
        if (page < 0 || size < 1) {
            throw new InvalidInputValueException("유효하지 않은 페이지/사이즈 값입니다.");
        }

        // (2) 문자열을 Enum으로 변환 (Optional 사용)
        Member.Role role = Optional.ofNullable(roleParam)
                .filter(param -> !param.isBlank())
                .map(param -> {
                    try {
                        return Member.Role.valueOf(param.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new InvalidInputValueException("유효하지 않은 role 값입니다. " + param);
                    }
                }).orElse(null);

        Member.Status status = Optional.ofNullable(statusParam)
                .filter(param -> !param.isBlank())
                .map(param -> {
                    try {
                        return Member.Status.valueOf(param.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new InvalidInputValueException("유효하지 않은 status 값입니다. " + param);
                    }
                }).orElse(null);

        // (3) 검색어 null 처리
        String sanitizedKeyword = Optional.ofNullable(keyword)
                .filter(param -> !param.isBlank())
                .orElse(null);

        // (4) 정렬 필드 검증 및 기본값 설정
        List<String> allowedSortFields = List.of("id", "name", "email", "created_at", "updated_at", "role");
        String sortProperty = Optional.ofNullable(sortField)
                .filter(param -> !param.isBlank() && allowedSortFields.contains(param))
                .orElse("id"); // 기본 정렬 필드는 "id"

        // (5) 정렬 방향 설정
        Sort.Direction direction = Optional.ofNullable(sortDirection)
                .filter(param -> !param.isBlank())
                .map(param -> "desc".equalsIgnoreCase(param) ? Sort.Direction.DESC : Sort.Direction.ASC)
                .orElse(sortProperty.equals("id") ? Sort.Direction.DESC : Sort.Direction.ASC); // id는 기본적으로 DESC, 나머지는 ASC

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortProperty));

        // (6) 페이징 처리
        Page<Member> memberPage = memberRepository.findAllWithFilters(role, status, sanitizedKeyword, pageable);

        // (7) 페이지 번호 검증
        if (memberPage.getTotalPages() > 0 && page >= memberPage.getTotalPages()) {
            throw new InvalidInputValueException("페이지 번호가 범위를 벗어났습니다.");
        }

        // (8) 결과 DTO 변환
        return MemberListResponseDto.fromEntityPage(memberPage);
    }

    // 회원 등록
    @Override
    public MemberResponseDto registerMember(MemberRegisterDto dto) {
        // 이메일 중복 체크
        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new MemberException("이미 사용중인 이메일입니다. "+ dto.getEmail(), ErrorCode.DUPLICATE_EMAIL);
        }

        // 조직(Organization) 존재 여부 확인
        Organization organization = organizationRepository.findById(dto.getOrganizationId())
            .orElseThrow(() -> new OrganizationException("조직이 존재하지 않습니다. id: " + dto.getOrganizationId(),
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
    @Override
    public MemberResponseDto updateMember(Long memberId, MemberUpdateDto dto) {
        // 기존 회원 찾기
        Member existingMember = memberRepository.findById(memberId)
            .orElseThrow(
                () -> new MemberNotFoundException("회원이 존재하지 않습니다. id: " + memberId));

        // 삭제된 회원이면 수정 불가
        if (existingMember.isDeleted()) {
            throw new DeletedMemberException();
        }

        // DTO -> 엔티티 업데이트
        MemberUpdateDto.toEntity(existingMember, dto);

        // 수정된 엔티티 저장
        Member updatedMember = memberRepository.save(existingMember);

        // DTO로 변환하여 반환
        return MemberResponseDto.fromEntity(updatedMember);
    }

    // 비밀번호 변경
    @Override
    public void changePassword(Long memberId, ChangePasswordDto dto) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(
                () -> new MemberNotFoundException("회원이 존재하지 않습니다. id: " + memberId));

        // 삭제된 회원이면 수정 불가
        if (member.isDeleted()) {
            throw new DeletedMemberException();
        }

        // 현재 비밀번호 확인, TODO 추후 현재 비밀번호 확인 로직 추가
//        if (!passwordEncoder.matches(dto.getCurrentPassword(), member.getPassword())) {
//            throw new MemberException("현재 비밀번호가 일치하지 않습니다.", ErrorCode.INVALID_LOGIN_CREDENTIALS);
//        }

        // 새 비밀번호와 확인 비밀번호 일치 여부 확인
        if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            throw new MemberException("새 비밀번호와 확인 비밀번호가 일치하지 않습니다.",
                ErrorCode.INVALID_LOGIN_CREDENTIALS);
        }

        // 비밀번호 암호화 적용
        String encodedPassword = passwordEncoder.encode(dto.getNewPassword());

        // 엔티티에 비밀번호 변경 로직 적용
        ChangePasswordDto.updatePassword(member, encodedPassword);

        // 수정된 엔티티 저장
        memberRepository.save(member);
    }

    // 회원 탈퇴
    @Override
    public void deleteMember(Long memberId, String reasonForDelete) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(
                () -> new MemberNotFoundException("회원이 존재하지 않습니다. id: " + memberId));
        if (member.isDeleted()) {
            throw new DeletedMemberException();
        }
        // 회원 삭제 처리
        member.deleteAccount(reasonForDelete);
        memberRepository.save(member);
    }

    //업체별 회원 목록 조회
    @Override
    public MemberListResponseDto getMembersByOrganizationId(Long organizationId, int page,
        int size) {

        //존재하지 않는 업체 아이디인 경우 예외 처리
        if (!organizationRepository.existsById(organizationId)) {
            throw new OrganizationNotFoundEntityException();
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

        // MemberListResponseDto로 변환
        return MemberListResponseDto.fromEntityPage(memberPage);
    }

    /**
     * 회원 서명 파일 업로드
     *
     * @param signatureFile 서명 파일
     * @return MemberSignatureResponseDto
     */
    @Override
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
    @Override
    public MemberSignatureExistResponseDto getSignature() {

        Member member = currentMemberUtil.getCurrentMember();

        return MemberSignatureExistResponseDto.toDto(member);
    }

    /** 회원 활성화
     *  관리자가 회원을 활성화 처리합니다. - activateAccount
     *  */
    @Override
    public void activateMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(
                () -> new MemberNotFoundException("회원이 존재하지 않습니다. id: " + memberId));

        if(member.isActive()){
            throw new MemberException("이미 활성화된 회원입니다.", ErrorCode.ALREADY_APPLIED);
        }
        member.activateAccount();
        memberRepository.save(member);

        // Redis 1번 저장소에 비활성화된 회원 ID 삭제
        if(redisConnectionCheckService.isRedisAvailable()){
            redisTemplateForInactiveMemers.delete("inactive:member:" + memberId);
        }
    }

    /**
     * 회원 비활성화
     * 관리자가 회원을 비활성화 처리합니다. - inactiveAccount
     * */
    @Override
    public void deactivateMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(
                () -> new MemberNotFoundException("회원이 존재하지 않습니다. id: " + memberId));

        if(member.isDeleted()){
            throw new DeletedMemberException();
        }

        if(!member.isActive()){
            throw new MemberException("이미 비활성화된 회원입니다.", ErrorCode.ALREADY_APPLIED);
        }
        member.deactivateAccount();
        memberRepository.save(member);

        // Redis 1번 저장소에 비활성화된 회원 ID 저장
        if(redisConnectionCheckService.isRedisAvailable()){
            redisTemplateForInactiveMemers.opsForValue().set("inactive:member:" + memberId, "true");
        }
    }

    @Override
    public String getMemberStatus(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(
                () -> new MemberNotFoundException("회원이 존재하지 않습니다. id: " + memberId));
        return member.getStatus().name();
    }

    @Override
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
