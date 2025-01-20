package com.checkping.service.question;

import com.checkping.domain.question.Question;
import com.checkping.domain.question.QuestionComment;
import com.checkping.domain.question.TaskBoardFile;
import com.checkping.domain.question.TaskBoardLink;
import com.checkping.dto.question.link.TaskBoardLinkRequest;
import com.checkping.dto.question.TaskBoardRequest;
import com.checkping.dto.question.TaskBoardRequest.RegisterDto;
import com.checkping.dto.question.TaskBoardRequest.SearchCondition;
import com.checkping.dto.question.TaskBoardRequest.UpdateDto;
import com.checkping.dto.question.TaskBoardResponse;
import com.checkping.dto.question.TaskBoardResponse.TaskBoardItemDto;
import com.checkping.dto.question.TaskBoardResponse.TaskBoardListDto;
import com.checkping.exception.question.TaskBoardNotFoundEntityException;
import com.checkping.infra.repository.question.TaskBoardReader;
import com.checkping.infra.repository.question.TaskBoardStore;
import com.checkping.infra.repository.question.comment.TaskBoardCommentReader;
import com.checkping.infra.repository.question.comment.TaskBoardCommentStore;
import com.checkping.infra.repository.question.file.TaskBoardFileStore;
import com.checkping.infra.repository.question.link.TaskBoardLinkStore;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class TaskBoardServiceImpl implements TaskBoardService {

    private final TaskBoardStore taskBoardStore;
    private final TaskBoardReader taskBoardReader;
    private final TaskBoardCommentReader taskBoardCommentReader;
    private final TaskBoardCommentStore taskBoardCommentStore;
    private final TaskBoardLinkStore taskBoardLinkStore;
    private final TaskBoardFileStore taskBoardFileStore;

    /**
     * 업무 관리 게시글 등록하기
     *
     * @param request  업무 관리 게시글에 필요한 request
     * @param fileList 첨부 파일 리스트
     * @return 생성한 Question 의 Dto
     */
    @Override
    public TaskBoardItemDto register(RegisterDto request, List<MultipartFile> fileList) {

        // dto -> entity
        Question initQuestion = TaskBoardRequest.RegisterDto.toEntity(request);
        initQuestion.activate();

        // save Question entity
        Question question = taskBoardStore.store(initQuestion);

        // Save File in S3
        List<TaskBoardFile> taskBoardFileList = taskBoardFileStore.saveFileList(question,
            fileList);

        // Add TaskBoardFile List
        question.addFile(taskBoardFileList);

        // get register info
        List<TaskBoardLinkRequest.RegisterDto> linkDtoList = request.getTaskBoardLinkList();

        // loop for add taskBoardLinkRequest
        for (TaskBoardLinkRequest.RegisterDto linkDto : linkDtoList) {

            // TaskBoardLink Dto -> Entity
            TaskBoardLink initTaskBoardLink = TaskBoardLinkRequest.RegisterDto.toEntity(question,
                linkDto);

            // save TaskBoardLink
            TaskBoardLink taskBoardLink = taskBoardLinkStore.store(initTaskBoardLink);

            // ADD TaskBoardLink (in Question)
            question.addLink(taskBoardLink);
        }

        // Entity -> Dto
        return TaskBoardItemDto.toDto(question);
    }

    /**
     * Question 조회 하기 (게시글 유형, 게시글 상태 별 필터링)
     *
     * @param searchCondition RequestParam 에서 받아오는 String 을 관리하는 타입
     * @return 조회한 TaskBoardListDto 의 리스트
     */
    @Override
    public List<TaskBoardListDto> getTaskBoardList(SearchCondition searchCondition) {

        // 조회
        List<Question> questionList = taskBoardReader.getTaskBoard(
            searchCondition.getBoardCategory(),
            searchCondition.getBoardStatus(),
            searchCondition.getKeyword());

        // Question -> TaskBoardListDto
        return questionList.stream().map(TaskBoardListDto::toDto).toList();
    }

    /**
     * 업무 관리 게시글 서비스 - 상세 조회
     *
     * @param taskBoardId 업무 관리 게시글 ID
     * @return TaskBoardListDto
     */
    @Override
    public TaskBoardItemDto getTaskBoardById(Long taskBoardId) {

        // find Question Entity
        Question question = taskBoardReader.getTaskBoardById(taskBoardId).orElseThrow(
            TaskBoardNotFoundEntityException::new);

        // Entity -> Dto
        return TaskBoardItemDto.toDto(question);
    }

    /**
     * 업무 관리 게시글 서비스 - SOFT DELETE
     *
     * @param taskBoardId 업무 관리 게시글 ID
     * @return 상태 변경이 된 업무 관리 게시글 Dto
     */
    @Override
    public TaskBoardListDto deleteSoft(Long taskBoardId) {

        // find Question Entity
        Question initQuestion = taskBoardReader.getTaskBoardById(taskBoardId).orElseThrow(
            TaskBoardNotFoundEntityException::new);

        // QuestionComment - SOFT DELETE
        List<QuestionComment> commentList = initQuestion.getCommentList();
        for (QuestionComment comment : commentList) {
            comment.deactivate();
            taskBoardCommentStore.store(comment);
        }

        // Question - SOFT DELETE
        initQuestion.deactivate();

        // save
        Question deletedQuestion = taskBoardStore.store(initQuestion);

        // Entity -> Dto
        return TaskBoardResponse.TaskBoardListDto.toDto(deletedQuestion);
    }

    /**
     * 업무 관리 게시글 서비스 - HARD DELETE
     *
     * @param taskBoardId 업무 관리 게시글 ID
     * @return HARD DELETE 를 요청한 업무 관리 게시글 Dto
     */
    @Override
    public TaskBoardListDto deleteHard(Long taskBoardId) {

        // find Question Entity
        Question initQuestion = taskBoardReader.getTaskBoardById(taskBoardId).orElseThrow(
            TaskBoardNotFoundEntityException::new);

        // QuestionComment - HARD DELETE
        List<QuestionComment> commentList = initQuestion.getCommentList();
        for (QuestionComment comment : commentList) {
            taskBoardCommentStore.deleteHard(comment);
        }

        // Question - HARD DELETE
        taskBoardStore.deleteHard(initQuestion);

        return TaskBoardResponse.TaskBoardListDto.toDto(initQuestion);
    }

    /**
     * 업무 관리 게시글 서비스 - 수정 기능
     *
     * @param taskBoardId 업무 관리 게시글 ID
     * @param request     업무 관리 게시글 수정 요청 Dto
     * @return 수정을 완료한 업무 관리 게시글 Dto
     */
    @Override
    public TaskBoardItemDto update(Long taskBoardId, UpdateDto request) {

        // find Question Entity
        Question initQuestion = taskBoardReader.getTaskBoardById(taskBoardId).orElseThrow(
            TaskBoardNotFoundEntityException::new);

        // update
        String title = request.getTitle();
        String content = request.getContent();
        initQuestion.update(title, content);

        // save
        Question updatedQuestion = taskBoardStore.store(initQuestion);

        // Entity -> Dto
        return TaskBoardResponse.TaskBoardItemDto.toDto(updatedQuestion);
    }
}
