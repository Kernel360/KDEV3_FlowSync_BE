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
public class TaskBoardReaderImpl implements TaskBoardReader {

    private final TaskBoardRepository taskBoardRepository;

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
            return taskBoardRepository.findByBoardCategoryAndBoardStatusAndTitleContaining(boardCategory,
                boardStatus, keyword);
        }

        // boardCategory AND boardStatus
        if (boardCategory != null && boardStatus != null) {
            return taskBoardRepository.findByBoardCategoryAndBoardStatus(boardCategory,
                boardStatus);
        }

        // boardCategory AND keyword
        if (boardCategory != null && keyword != null) {
            return taskBoardRepository.findTaskBoardByBoardCategoryAndTitleContaining(boardCategory, keyword);
        }

        // boardStatus AND keyword
        if (boardStatus != null && keyword != null) {
            return taskBoardRepository.findTaskBoardByBoardStatusAndTitleContaining(boardStatus, keyword);
        }

        // boardCategory
        if (boardCategory != null) {
            return taskBoardRepository.findByBoardCategory(boardCategory);
        }

        // boardStatus
        if (boardStatus != null) {
            return taskBoardRepository.findByBoardStatus(boardStatus);
        }

        // keyword
        if (keyword != null) {
            return taskBoardRepository.findByTitleContaining(keyword);
        }

        // 조회
        return taskBoardRepository.findAll();

    }

    /**
     * Question 조회 기능
     *
     * @param id question 아이디
     * @return Question 조회 결과
     */
    @Override
    public Optional<Question> getTaskBoardById(Long id) {
        return taskBoardRepository.findById(id);
    }
}
