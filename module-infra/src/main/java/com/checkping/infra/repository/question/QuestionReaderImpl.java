package com.checkping.infra.repository.question;

import com.checkping.domain.question.Question;
import com.checkping.domain.question.Question.BoardCategory;
import com.checkping.domain.question.Question.BoardStatus;
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
     * @param boardCategory Question.BoardCategory
     * @param boardStatus   Question.BoardStatus
     * @param keyword       검색어
     * @return Question 전체 조회
     */
    @Override
    public List<Question> getTaskBoard(BoardCategory boardCategory, BoardStatus boardStatus,
        String keyword) {

        // keyword, boardCategory, boardStatus
        if (boardCategory != null && boardStatus != null && keyword != null) {
            return questionRepository.findByBoardCategoryAndBoardStatusAndTitleContaining(boardCategory,
                boardStatus, keyword);
        }

        // boardCategory AND boardStatus
        if (boardCategory != null && boardStatus != null) {
            return questionRepository.findByBoardCategoryAndBoardStatus(boardCategory,
                boardStatus);
        }

        // boardCategory AND keyword
        if (boardCategory != null && keyword != null) {
            return questionRepository.findTaskBoardByBoardCategoryAndTitleContaining(boardCategory, keyword);
        }

        // boardStatus AND keyword
        if (boardStatus != null && keyword != null) {
            return questionRepository.findTaskBoardByBoardStatusAndTitleContaining(boardStatus, keyword);
        }

        // boardCategory
        if (boardCategory != null) {
            return questionRepository.findByBoardCategory(boardCategory);
        }

        // boardStatus
        if (boardStatus != null) {
            return questionRepository.findByBoardStatus(boardStatus);
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
