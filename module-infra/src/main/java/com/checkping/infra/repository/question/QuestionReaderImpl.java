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

        return questionRepository.getByCondition(projectId, searchCondition, pageable);
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
     * Question 조회 기능 (Comment 포함)
     *
     * @param questionId question 아이디
     * @return Question 조회 결과
     */
    @Override
    public Optional<Question> getByIdWithComments(Long questionId) {
        return questionRepository.findByIdWithComments(questionId);
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
        return questionRepository.existsByProjectIdAndId(projectId, questionId);
    }
}
