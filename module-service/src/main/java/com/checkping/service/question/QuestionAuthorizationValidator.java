package com.checkping.service.question;

import com.checkping.common.utils.AbstractAuthorizationValidator;
import com.checkping.domain.member.Member;
import com.checkping.exception.project.ProjectNotMemberException;
import com.checkping.infra.repository.project.ProjectReader;
import com.checkping.infra.repository.question.QuestionReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionAuthorizationValidator extends AbstractAuthorizationValidator {

    private final ProjectReader projectReader;
    private final QuestionReader questionReader;

    /**
     * 질문 게시글 접근 권한 확인
     *
     * @param projectId 프로젝트 엔티티
     * @param member    멤버 엔티티
     */
    public void validateAccessibleQuestion(Long projectId, Member member) {
        // 관리자는 모든 행동 가능
        if (checkAdmin(member)) {
            return;
        }

        validate(checkProjectMember(projectId, member), ProjectNotMemberException::new);
    }


    /**
     * 관리자 권한 확인
     *
     * @param member 멤버 엔티티
     * @return 관리자 여부
     */
    private boolean checkAdmin(Member member) {
        return member.isAdmin();
    }

    /**
     * 프로젝트에 속한 회원인지 확인
     *
     * @param projectId 프로젝트 id
     * @param member  멤버 엔티티
     * @return 프로젝트에 속한 회원 여부
     */
    private boolean checkProjectMember(Long projectId, Member member) {
        return projectReader.matchProjectAndOrganization(projectId,
            member.getOrganization().getId());
    }
}
