package com.checkping.service.question;

import com.checkping.domain.project.Project;
import com.checkping.domain.question.Question;
import com.checkping.domain.question.QuestionComment;
import com.checkping.domain.question.QuestionFile;
import com.checkping.domain.question.QuestionLink;
import com.checkping.dto.question.QuestionRegister;
import com.checkping.dto.question.QuestionRequest.SearchCondition;
import com.checkping.dto.question.QuestionRequest.UpdateDto;
import com.checkping.dto.question.QuestionResponse.QuestionItemDto;
import com.checkping.dto.question.QuestionResponse.QuestionListDto;
import com.checkping.dto.question.link.QuestionLinkRequest;
import com.checkping.exception.question.QuestionNotFoundEntityException;
import com.checkping.infra.repository.project.ProjectReader;
import com.checkping.infra.repository.question.QuestionReader;
import com.checkping.infra.repository.question.QuestionStore;
import com.checkping.infra.repository.question.comment.QuestionCommentReader;
import com.checkping.infra.repository.question.comment.QuestionCommentStore;
import com.checkping.infra.repository.question.file.QuestionFileStore;
import com.checkping.infra.repository.question.link.QuestionLinkStore;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionStore questionStore;
    private final QuestionReader questionReader;
    private final QuestionCommentReader questionCommentReader;
    private final QuestionCommentStore questionCommentStore;
    private final QuestionLinkStore questionLinkStore;
    private final QuestionFileStore questionFileStore;
    private final ProjectReader projectReader;

    /**
     * 업무 관리 게시글 등록하기
     *
     * @param projectId 프로젝트 ID
     * @param request   업무 관리 게시글에 필요한 request
     * @return 생성한 Question 의 Dto
     */
    @Override
    public QuestionItemDto register(Long projectId, QuestionRegister.Request request) {

        // Project ID -> Project Entity
        Project project = projectReader.getById(projectId);

        // Question Dto -> Question Entity
        Question initQuestion = QuestionRegister.Request.toEntity(request);
        initQuestion.activate();
        initQuestion.updateCategory(Question.Category.QUESTION);
        initQuestion.updateStatus(Question.Status.WAIT);
        initQuestion.containedProject(project);

        // save Question entity
        Question question = questionStore.store(initQuestion);

        // Save & Add QuestionFile List
        List<QuestionFile> questionFileList = questionFileStore.storeFileList(question,
            request.getFileInfoList());
        question.addFile(questionFileList);

        // get register info
        List<QuestionLinkRequest.RegisterDto> linkDtoList = request.getLinkList();

        // loop for add taskBoardLinkRequest
        for (QuestionLinkRequest.RegisterDto linkDto : linkDtoList) {

            // QuestionLink Dto -> Entity
            QuestionLink initQuestionLink = QuestionLinkRequest.RegisterDto.toEntity(question,
                linkDto);

            // save QuestionLink
            QuestionLink questionLink = questionLinkStore.store(initQuestionLink);

            // ADD QuestionLink (in Question)
            question.addLink(questionLink);
        }

        // Entity -> Dto
        return QuestionItemDto.toDto(question);
    }

    /**
     * Question 조회 하기 (게시글 유형, 게시글 상태 별 필터링)
     *
     * @param searchCondition RequestParam 에서 받아오는 String 을 관리하는 타입
     * @return 조회한 QuestionListDto 의 리스트
     */
    @Override
    public List<QuestionListDto> getQuestionList(SearchCondition searchCondition) {

        // 조회
        List<Question> questionList = questionReader.getQuestion(
            searchCondition.getCategory(),
            searchCondition.getStatus(),
            searchCondition.getKeyword());

        // Question -> QuestionListDto
        return questionList.stream().map(QuestionListDto::toDto).toList();
    }

    /**
     * 업무 관리 게시글 서비스 - 상세 조회
     *
     * @param taskBoardId 업무 관리 게시글 ID
     * @return QuestionListDto
     */
    @Override
    public QuestionItemDto getQuestionById(Long taskBoardId) {

        // find Question Entity
        Question question = questionReader.getQuestionById(taskBoardId).orElseThrow(
            QuestionNotFoundEntityException::new);

        // Entity -> Dto
        return QuestionItemDto.toDto(question);
    }

    /**
     * 업무 관리 게시글 서비스 - SOFT DELETE
     *
     * @param taskBoardId 업무 관리 게시글 ID
     * @return 상태 변경이 된 업무 관리 게시글 Dto
     */
    @Override
    public QuestionListDto deleteSoft(Long taskBoardId) {

        // find Question Entity
        Question initQuestion = questionReader.getQuestionById(taskBoardId).orElseThrow(
            QuestionNotFoundEntityException::new);

        // QuestionComment - SOFT DELETE
        List<QuestionComment> commentList = initQuestion.getCommentList();
        for (QuestionComment comment : commentList) {
            comment.deactivate();
            questionCommentStore.store(comment);
        }

        // Question - SOFT DELETE
        initQuestion.deactivate();

        // save
        Question deletedQuestion = questionStore.store(initQuestion);

        // Entity -> Dto
        return QuestionListDto.toDto(deletedQuestion);
    }

    /**
     * 업무 관리 게시글 서비스 - HARD DELETE
     *
     * @param taskBoardId 업무 관리 게시글 ID
     * @return HARD DELETE 를 요청한 업무 관리 게시글 Dto
     */
    @Override
    public QuestionListDto deleteHard(Long taskBoardId) {

        // find Question Entity
        Question initQuestion = questionReader.getQuestionById(taskBoardId).orElseThrow(
            QuestionNotFoundEntityException::new);

        // QuestionComment - HARD DELETE
        List<QuestionComment> commentList = initQuestion.getCommentList();
        for (QuestionComment comment : commentList) {
            questionCommentStore.deleteHard(comment);
        }

        // Question - HARD DELETE
        questionStore.deleteHard(initQuestion);

        return QuestionListDto.toDto(initQuestion);
    }

    /**
     * 업무 관리 게시글 서비스 - 수정 기능
     *
     * @param taskBoardId 업무 관리 게시글 ID
     * @param request     업무 관리 게시글 수정 요청 Dto
     * @return 수정을 완료한 업무 관리 게시글 Dto
     */
    @Override
    public QuestionItemDto update(Long taskBoardId, UpdateDto request) {

        // find Question Entity
        Question initQuestion = questionReader.getQuestionById(taskBoardId).orElseThrow(
            QuestionNotFoundEntityException::new);

        // update
        String title = request.getTitle();
        String content = request.getContent();
        initQuestion.update(title, content);

        // save
        Question updatedQuestion = questionStore.store(initQuestion);

        // Entity -> Dto
        return QuestionItemDto.toDto(updatedQuestion);
    }
}
