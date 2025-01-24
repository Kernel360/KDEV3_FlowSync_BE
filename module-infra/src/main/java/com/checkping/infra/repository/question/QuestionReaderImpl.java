package com.checkping.infra.repository.question;

import com.checkping.domain.question.Question;
import com.checkping.info.question.QuestionSearchInfo;
import io.awspring.cloud.s3.S3OutputStreamProvider;
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
    private final S3OutputStreamProvider s3OutputStreamProvider;

    /**
     * Question 검색 기능
     * TODO : 동적 쿼리가 가능하도록 변경
     *
     * @param projectId 프로젝트 아이디
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
        boolean isCategory = searchCondition.category() != null;
        boolean isStatus = searchCondition.status() != null;

        // Search all
        if (!isCategory && !isStatus && !isKeyword) {
            return questionRepository.findByProjectId(
                projectId, pageable);
        }

        // Search keyword
        if (!isCategory && !isStatus && isKeyword) {
            return questionRepository.findByProjectIdAndTitleContaining(
                projectId, searchCondition.keyword(), pageable);
        }

        // Search status
        if (!isCategory && isStatus && !isKeyword) {
            return questionRepository.findByProjectIdAndStatus(
                projectId, searchCondition.status(), pageable);
        }

        // Search category
        if (isCategory && !isStatus && !isKeyword) {
            return questionRepository.findByProjectIdAndCategory(
                projectId, searchCondition.category(), pageable);
        }

        // Search category AND status
        if (isCategory && isStatus && !isKeyword) {
            return questionRepository.findByProjectIdAndCategoryAndStatus(
                projectId, searchCondition.category(), searchCondition.status(), pageable);
        }

        // Search category AND keyword
        if (isCategory && !isStatus && isKeyword) {
            return questionRepository.findByProjectIdAndCategoryAndTitleContaining(
                projectId, searchCondition.category(), searchCondition.keyword(), pageable);
        }

        // Search status AND keyword
        if (!isCategory && isStatus && isKeyword) {
            return questionRepository.findByProjectIdAndStatusAndTitleContaining(
                projectId, searchCondition.status(), searchCondition.keyword(), pageable);
        }

        // Search category AND status AND keyword
        return questionRepository.findByProjectIdAndCategoryAndStatusAndTitleContaining(
            projectId, searchCondition.category(), searchCondition.status(),
            searchCondition.keyword(), pageable);
    }

    /**
     * Question 조회 기능
     *
     * @param id question 아이디
     * @return Question 조회 결과
     */
    @Override
    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }
}
