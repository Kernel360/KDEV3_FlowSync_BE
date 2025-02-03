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

        //프로젝트
        if (requestURI.matches("^/admins/projects/\\d+/projectInfo$") ||
                requestURI.matches("^/projects/\\d+/projectInfo$")) {

            log.info("프로젝트 상세 조회 시 체크");

            // projectId 추출
            String[] uriParts = requestURI.split("/");
            Long projectId = Long.parseLong(uriParts[uriParts.length - 2]); // 두 번째 마지막 값이 projectId

            boolean exist = memberByProjectService.existsByMemberIdAndProjectId(getAuthenticatedUserId(), projectId);

            if(!exist) {
                throw new BaseException("해당 프로젝트에 접근권한이 없습니다.",ErrorCode.BAD_REQUEST);
            }
            return true;
        }

        //질문 게시판

        //결재 게시판

        //결재 요청


        return true;
    }



    /**
     * 로그인한 유저 ID 가져오기
     *
     * @return 유저 ID
     */
    public Long getAuthenticatedUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getId();
        }

        return null;
    }
}