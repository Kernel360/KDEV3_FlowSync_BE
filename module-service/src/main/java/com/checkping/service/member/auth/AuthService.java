package com.checkping.service.member.auth;

import com.checkping.common.response.BaseResponse;
import com.checkping.domain.member.projection.ProjectList;
import com.checkping.dto.member.response.MemberResponseDto;
import com.checkping.exception.auth.InvalidTokenException;
import com.checkping.exception.auth.LoginFailureException;
import com.checkping.exception.auth.RefreshTokenNotFoundException;
import com.checkping.infra.repository.member.ProjectQueryRepository;
import com.checkping.service.member.util.CurrentMemberUtil;
import com.checkping.service.member.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CurrentMemberUtil currentMemberUtil;
    private final TokenBlacklistService tokenBlacklistService;
    private final RedisConnectionCheckService redisConnectionCheckService;
    private final ProjectQueryRepository projectQueryRepository;

    public BaseResponse getCurrentMember() {

        Long memberId = currentMemberUtil.getCurrentMember().getId();

        Page<ProjectList> result = projectQueryRepository.getProjectsByMemberAndOrganization(
                null,
                memberId,
                null,
                null,
                null);

        List<Long> projectIds = result.getContent().stream()
                .map(ProjectList::getId)
                .toList();

        return BaseResponse.success(MemberResponseDto.MeProjectResponseDto.fromEntity(currentMemberUtil.getCurrentMember(), projectIds));
    }

    /**
     * 로그인 처리
     * - 인증 실패 시 예외 던지기
     * - 인증 성공 시 access/refresh 토큰 생성 → 쿠키 설정(쿠키 세팅은 Controller에서 처리해도 됨)
     */
    public AuthTokens login(String email, String password) {
        try {
            // 1) 인증 시도
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(email, password);
            Authentication authResult = authenticationManager.authenticate(authToken);

            // 2) 인증 성공 시 사용자 정보 추출
            CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
            Long id = userDetails.getId();
            String role = authResult.getAuthorities().iterator().next().getAuthority();
            String name = userDetails.getName();

            // 3) JWT 생성
            String accessToken = jwtUtil.createJwt("access", id, name, email, role, 15);
            String refreshToken = jwtUtil.createJwt("refresh", id, name, email, role, 1440);

            // 4) 토큰을 반환
            return new AuthTokens(accessToken, refreshToken);

        } catch (Exception e) {
            // 인증 실패 시 예외 던짐
            System.out.println(e);
            throw new LoginFailureException();
        }
    }

    /**
     * 로그아웃 처리
     * - refresh 쿠키가 없거나 유효하지 않으면 예외 던지기
     */
    public void logout(HttpServletRequest request) {
        // Refresh Token 가져오기
        String refresh = JwtUtil.extractToken(request, "refresh");
        if (refresh == null) {
            throw new RefreshTokenNotFoundException();
        }

        // Access Token 가져오기
        String accessToken = JwtUtil.extractToken(request, "access");

        // 토큰 유효성 검증
        if (!"refresh".equals(jwtUtil.getCategory(refresh))) {
            throw new InvalidTokenException();
        }

        // 레디스 연결 여부 먼저 확인
        if (redisConnectionCheckService.isRedisAvailable()) {
            // 블랙리스트 추가
            tokenBlacklistService.blacklistRefreshToken(refresh, jwtUtil.getExpiration(refresh));
            if (accessToken != null) {
                tokenBlacklistService.blacklistAccessToken(accessToken, jwtUtil.getExpiration(accessToken));
            }
        }
    }
}