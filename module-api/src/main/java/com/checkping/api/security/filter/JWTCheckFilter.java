package com.checkping.api.security.filter;

import com.checkping.api.security.util.JWTUtil;
import com.checkping.service.member.security.MemberDto;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Slf4j
public class JWTCheckFilter extends OncePerRequestFilter {
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        String path = request.getRequestURI();

        log.info("check uri..................." + path);

        // /admin/member/login 경로의 호출은 체크하지 않음
        if (path.startsWith("/admins/members/login")) {
            return true;
        }

        if (path.startsWith("/admins/organizations")) {
            return true;
        }

        if (path.startsWith("/admins/members")) {
            return true;
        }

        if (path.startsWith("/admins/members/refresh")) {
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("---------------------------JWTCheckFilter Start--------------------------------");

        String authHeaderStr = request.getHeader("Authorization");
        try {
            //Bearer accestoken.....
            String accessToken = authHeaderStr.substring("Bearer ".length());
            Map<String, Object> claims = JWTUtil.validateToken(accessToken);

            log.info("JWT claims: " + claims);

            String email = (String) claims.get("email");
            String name = (String) claims.get("name");
            String phoneNum = (String) claims.get("phoneNum");
            String role = (String) claims.get("role");
            String organizationId = (String) claims.get("organizationId");
            String jobRole = (String) claims.get("jobRole");
            String jobTitle = (String) claims.get("jobTitle");
            String introduction = (String) claims.get("introduction");

            MemberDto memberDto = new MemberDto(
                    email,
                    null,
                    role,
                    name,
                    phoneNum,
                    jobRole,
                    jobTitle,
                    introduction,
                    organizationId
            );

            log.info("----------------------------------");
            log.info("memberDto: " + memberDto);
            log.info(memberDto.getAuthorities().toString());

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(memberDto,null, memberDto.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);

        } catch (Exception e) {

            log.error("JWT Check Error..........");
            log.error(e.getMessage());

            Gson gson = new Gson();
            String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));

            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            printWriter.print(msg);
            printWriter.close();
        }

        log.info("---------------------------JWTCheckFilter End--------------------------------");
    }
}