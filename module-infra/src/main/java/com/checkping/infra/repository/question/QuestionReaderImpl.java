package com.checkping.infra.repository.question;

import com.checkping.domain.question.Question;
import com.checkping.info.question.QuestionSearchInfo;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class QuestionReaderImpl implements QuestionReader {

    private final QuestionRepository questionRepository;

    /**
     * Question 검색 기능
     * TODO : 동적 쿼리가 가능하도록 변경
     *
     * @param projectId       프로젝트 아이디
     * @param searchCondition 검색 조건
     * @return Question 검색 결과
     */
    @Override
    public Page<Question> searchQuestions(Long projectId,
        QuestionSearchInfo.SearchCondition searchCondition) {

        // 페이지 객체 생성
        Pageable pageable = PageRequest.of(searchCondition.currentPage(),
            searchCondition.pageSize());

        // keyword / category / status 검색 조건 여부 확인
        boolean isKeyword = searchCondition.keyword() != null && !searchCondition.keyword()
            .isEmpty();
        boolean isProgressStep = searchCondition.progressId() != null;
        boolean isStatus = searchCondition.status() != null;

        // Search all
        if (!isProgressStep && !isStatus && !isKeyword) {
            return questionRepository.findByProjectId(
                projectId, pageable);
        }

        // Search keyword
        if (!isProgressStep && !isStatus && isKeyword) {
            return questionRepository.findByProjectIdAndTitleContaining(
                projectId, searchCondition.keyword(), pageable);
        }

        // Search status
        if (!isProgressStep && isStatus && !isKeyword) {
            return questionRepository.findByProjectIdAndStatus(
                projectId, searchCondition.status(), pageable);
        }

        // Search category
        if (isProgressStep && !isStatus && !isKeyword) {
            return questionRepository.findByProjectIdAndProgressStepId(
                projectId, searchCondition.progressId(), pageable);
        }

        // Search category AND status
        if (isProgressStep && isStatus && !isKeyword) {
            return questionRepository.findByProjectIdAndProgressStepIdAndStatus(
                projectId, searchCondition.progressId(), searchCondition.status(), pageable);
        }

        // Search category AND keyword
        if (isProgressStep && !isStatus && isKeyword) {
            return questionRepository.findByProjectIdAndProgressStepIdAndTitleContaining(
                projectId, searchCondition.progressId(), searchCondition.keyword(), pageable);
        }

        // Search status AND keyword
        if (!isProgressStep && isStatus && isKeyword) {
            return questionRepository.findByProjectIdAndStatusAndTitleContaining(
                projectId, searchCondition.status(), searchCondition.keyword(), pageable);
        }

        // Search category AND status AND keyword
        return questionRepository.findByProjectIdAndProgressStepIdAndStatusAndTitleContaining(
            projectId, searchCondition.progressId(), searchCondition.status(),
            searchCondition.keyword(), pageable);
    }

    /**
     * Question 조회 기능
     *
     * @param questionId question 아이디
     * @return Question 조회 결과
     */
    @Override
    public Optional<Question> getById(Long questionId) {
        return questionRepository.findById(questionId);
    }

    /**
     * 프로젝트별 Question 개수 조회
     *
     * @param projectId project id
     * @return 프로젝트별 Question 개수
     */
    @Override
    public Long countQuestionsByProject(Long projectId) {
        return questionRepository.countByProjectId(projectId);
    }

    /**
     * 진행상태별 Question 개수 조회
     *
     * @param projectId      project id
     * @param progressStepId progress step id
     * @return 진행상태별 Question 개수
     */
    @Override
    public Long countQuestionsByProgressStep(Long projectId, Long progressStepId) {
        return questionRepository.countByProjectIdAndProgressStepId(projectId, progressStepId);
    }

    /**
     * 프로젝트에 속한 Question 존재 여부 확인
     *
     * @param projectId  프로젝트 아이디
     * @param questionId 질문 아이디
     * @return 프로젝트에 속한 Question 존재 여부
     */
    @Override
    public boolean checkQuestionContaining(Long projectId, Long questionId) {
        return questionRepository.existsByIdAndProjectId(questionId, projectId);
    }
}
