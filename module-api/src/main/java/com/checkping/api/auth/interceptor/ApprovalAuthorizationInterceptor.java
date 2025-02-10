package com.checkping.api.auth.interceptor;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import com.checkping.service.member.MemberService;
import com.checkping.service.member.auth.CustomUserDetails;
import com.checkping.service.permission.MemberByProjectService;
import com.checkping.service.project.ProjectServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
@RequiredArgsConstructor
// TODO : AuthInterceptor 와 코드 따로 분리할 지 합칠 지 결정해서 수정
public class ApprovalAuthorizationInterceptor implements HandlerInterceptor {

    private final MemberByProjectService memberByProjectService;
    private final ProjectServiceImpl projectServiceImpl;
    private final MemberService memberService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        String method = request.getMethod(); // HTTP 메서드 가져오기
        log.info("Request URI: {}, Method: {}", requestURI, method);

        // 현재 로그인한 사용자 가져오기
        CustomUserDetails authenticatedUser = getAuthenticatedUser();

        // 어드민은 모두 가능
        if (authenticatedUser != null) {
            if (getAuthenticatedUser().getRole().equals("ROLE_ADMIN")){
                return true;
            }
        }

        // 결재 조회(GET)기능 인가 검증 수행 - 어드민/ 프로젝트 고객사 회원/ 프로젝트 개발자만 가능
        if (method.equalsIgnoreCase("GET") && (
                requestURI.matches("^/projects/\\d+/approvals$") ||
                requestURI.matches("^/projects/\\d+/approvals/\\d+$")
                )
            ) {
            log.info("결재 조회 인가 검사 대상 API: {}", requestURI);

            // projectId 추출
            String[] uriParts = requestURI.split("/");
            Long projectId;

            if (requestURI.matches("^/projects/\\d+/approvals$")) {
                projectId = Long.parseLong(uriParts[uriParts.length - 2]);
            }

            else {
                projectId = Long.parseLong(uriParts[uriParts.length - 3]);
            }

            // 프로젝트에 대한 멤버인지 확인
            boolean exist = memberByProjectService.existsByMemberIdAndProjectId(authenticatedUser.getId(), projectId);

            if (!exist) {
                throw new BaseException("이 프로젝트에 대한 권한이 없습니다.", ErrorCode.FORBIDDEN);
            }
            return true;
        }

        // 결재 생성, 수정, 삭제 권한 검사
        if ((method.equalsIgnoreCase("POST") && requestURI.matches("^/projects/\\d+/approvals$")) ||
                (method.equalsIgnoreCase("PUT") && requestURI.matches("^/projects/\\d+/approvals/\\d+$")) ||
                (method.equalsIgnoreCase("DELETE") && requestURI.matches("^/projects/\\d+/approvals/\\d+$"))) {

            log.info("결재 생성/수정/삭제 권한 검사: {}", requestURI);

            String[] uriParts = requestURI.split("/");

            Long projectId;

            if(method.equalsIgnoreCase("POST")){
                projectId = Long.parseLong(uriParts[uriParts.length - 2]); // 두 번째 마지막 값이 projectId
            }

            else {
                projectId = Long.parseLong(uriParts[uriParts.length - 3]); // 두 번째 마지막 값이 projectId
            }


            Long devOrgID = memberService.getMemberById(authenticatedUser.getId()).getOrganizationId();
            // 프로젝트의 개발사 소속 회원인지 확인
            boolean isDeveloper = projectServiceImpl.getUpdateProjectInfo(projectId).getDeveloperOrgId().equals(devOrgID);

            log.info("요청 회원 ID: " + authenticatedUser.getId());
            log.info("프로젝트 ID: " + projectId);
            log.info("요청 회원 소속 회사 ID: " + devOrgID);
            log.info("프로젝트 담당 개발사 ID: " + projectServiceImpl.getUpdateProjectInfo(projectId).getDeveloperOrgId());
            log.info("해당 프로젝트 개발사 회원 여부: " + isDeveloper);

            if (!isDeveloper) {
                throw new BaseException("이 프로젝트에 대한 권한이 없습니다. 관리자와 프로젝트 담당 개발사소속 회원만 가능합니다.", ErrorCode.FORBIDDEN);
            }
            return true;
        }


        //ㅇㅇㅇ
        if (requestURI.matches("^/projects/\\d+/approvals$") ||
                requestURI.matches("^/projects/\\d+/approvals/\\d+/confirm$")) {

            log.info("인가 검사 대상 API: {}", requestURI);

            // projectId 추출
            String[] uriParts = requestURI.split("/");
            Long projectId = Long.parseLong(uriParts[2]); // 세 번째 요소가 projectId

            boolean hasPermission = projectServiceImpl.getUpdateProjectInfo(projectId).getDevOwnerId().equals(authenticatedUser.getId());

            if (!hasPermission) {
                throw new BaseException("이 프로젝트에 대한 권한이 없습니다.", ErrorCode.FORBIDDEN);
            }
        }

        return true;
    }

    private CustomUserDetails getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails;
        }
        return null;
    }
}
