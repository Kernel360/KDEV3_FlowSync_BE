package com.checkping.infra.repository.question;

import com.checkping.domain.question.Question;
import com.checkping.domain.question.Question.Category;
import com.checkping.domain.question.Question.Status;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuestionReaderImpl implements QuestionReader {

    private final QuestionRepository questionRepository;

    /**
     * Question 전체 조회 및 필터링 조회
     *
     * @param category Question.Category
     * @param status   Question.Status
     * @param keyword       검색어
     * @return Question 전체 조회
     */
    @Override
    public List<Question> getTaskBoard(Category category, Status status,
        String keyword) {

        // keyword, category, status
        if (category != null && status != null && keyword != null) {
            return questionRepository.findByCategoryAndStatusAndTitleContaining(category,
                status, keyword);
        }

        // category AND status
        if (category != null && status != null) {
            return questionRepository.findByCategoryAndStatus(category,
                status);
        }

        // category AND keyword
        if (category != null && keyword != null) {
            return questionRepository.findTaskBoardByCategoryAndTitleContaining(category, keyword);
        }

        // status AND keyword
        if (status != null && keyword != null) {
            return questionRepository.findTaskBoardByStatusAndTitleContaining(status, keyword);
        }

        // category
        if (category != null) {
            return questionRepository.findByCategory(category);
        }

        // status
        if (status != null) {
            return questionRepository.findByStatus(status);
        }

        // keyword
        if (keyword != null) {
            return questionRepository.findByTitleContaining(keyword);
        }

        // 조회
        return questionRepository.findAll();

    }

    /**
     * Question 조회 기능
     *
     * @param id question 아이디
     * @return Question 조회 결과
     */
    @Override
    public Optional<Question> getTaskBoardById(Long id) {
        return questionRepository.findById(id);
    }
}
