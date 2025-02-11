package com.checkping.api.auth.interceptor;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import com.checkping.service.member.auth.CustomUserDetails;
import com.checkping.service.permission.MemberByProjectService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    private final MemberByProjectService memberByProjectService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        log.info("Request URI: {}", requestURI);

        if (getAuthenticatedUser() != null) {
            if (getAuthenticatedUser().getRole().equals("ROLE_ADMIN")){
                return true;
            }
        }

        if (requestURI.matches("^/projects/\\d+/approvals/\\d+/confirm$")) {
            return true;
        }

        // 프로젝트
        if (requestURI.matches("^/admins/projects/\\d+/project-info$") ||
                requestURI.matches("^/projects/\\d+/project-info$")) {

            log.info("프로젝트 interceptor");

            // projectId 추출
            String[] uriParts = requestURI.split("/");
            Long projectId = Long.parseLong(uriParts[uriParts.length - 2]); // 두 번째 마지막 값이 projectId

            boolean exist = memberByProjectService.existsByMemberIdAndProjectId(getAuthenticatedUser().getId(), projectId);

            if (!exist) {
                throw new BaseException("프로젝트 접근 권한이 없습니다.", ErrorCode.BAD_REQUEST);
            }
            return true;
        }

        // 질문/결재 게시판
        if (requestURI.matches("^/projects/\\d+/questions.*$") ||
                requestURI.matches("^/projects/\\d+/approvals.*$")) {

            log.info("게시판 interceptor");

            String[] uriParts = requestURI.split("/");
            Long projectId = Long.parseLong(uriParts[2]); // 세 번째 마지막 값이 projectId

            boolean exist = memberByProjectService.existsByMemberIdAndProjectId(getAuthenticatedUser().getId(), projectId);

            if (!exist) {
                throw new BaseException("게시판 접근 권한이 없습니다.", ErrorCode.BAD_REQUEST);
            }
            return true;
        }

        return true;
    }

    /**
     * 로그인한 유저 가져오기
     *
     * @return userDetails
     */
    public CustomUserDetails getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails;
        }
        return null;
    }
}