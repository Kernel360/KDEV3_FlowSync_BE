package com.checkping.service.question;

import com.checkping.domain.project.ProgressStep;
import com.checkping.domain.question.Question;
import com.checkping.domain.question.QuestionComment;
import com.checkping.domain.question.QuestionFile;
import com.checkping.domain.question.QuestionLink;
import com.checkping.dto.question.QuestionCounter;
import com.checkping.dto.question.QuestionGet;
import com.checkping.dto.question.QuestionRegister;
import com.checkping.dto.question.QuestionRegister.Request;
import com.checkping.dto.question.QuestionRequest.UpdateDto;
import com.checkping.dto.question.QuestionResponse.QuestionItemDto;
import com.checkping.dto.question.QuestionResponse.QuestionListDto;
import com.checkping.dto.question.QuestionSearch;
import com.checkping.dto.question.QuestionSearchCondition;
import com.checkping.dto.question.file.QuestionFileRegister;
import com.checkping.dto.question.link.QuestionLinkRegister;
import com.checkping.exception.question.QuestionNotFoundEntityException;
import com.checkping.info.question.QuestionSearchInfo;
import com.checkping.infra.repository.project.ProgressStepReader;
import com.checkping.infra.repository.project.ProjectReader;
import com.checkping.infra.repository.question.QuestionReader;
import com.checkping.infra.repository.question.QuestionStore;
import com.checkping.infra.repository.question.comment.QuestionCommentReader;
import com.checkping.infra.repository.question.comment.QuestionCommentStore;
import com.checkping.infra.repository.question.file.QuestionFileStore;
import com.checkping.infra.repository.question.link.QuestionLinkStore;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final ProgressStepReader progressStepReader;

    /**
     * 업무 관리 게시글 등록하기
     *
     * @param projectId 프로젝트 ID
     * @param request   업무 관리 게시글에 필요한 request
     * @return 생성한 Question 의 Dto
     */
    @Override
    public QuestionRegister.Response register(Long projectId, Request request) {

        // Question Dto -> Question Entity
        Question initQuestion = QuestionRegister.Request.toEntity(projectId, request);

        // save Question entity
        Question question = questionStore.store(initQuestion);

        // QuestionFileRequest.RegisterDto -> QuestionFile Entity
        List<QuestionFile> files = QuestionFileRegister.Request.toEntity(question,
            request.getFileInfoList());
        // Save & Add QuestionFile List
        questionFileStore.store(files);
        question.addFile(files);

        // QuestionLinkRequest.RegisterDto -> QuestionLink Entity
        List<QuestionLink> links = QuestionLinkRegister.Request.toEntity(question,
            request.getLinkList());
        // Save & Add QuestionLink
        questionLinkStore.store(links);
        question.addLink(links);

        // Entity -> Dto
        return QuestionRegister.Response.toDto(question);
    }

    /**
     * Question 조회 하기 (게시글 유형, 게시글 상태 별 필터링)
     *
     * @param projectId       프로젝트 ID
     * @param searchCondition RequestParam 에서 받아오는 String 을 관리하는 타입
     * @return 조회한 QuestionListDto 의 리스트
     */
    @Override
    public QuestionSearch.Response searchQuestions(Long projectId,
        QuestionSearchCondition searchCondition) {

        // RequestParam -> Info
        QuestionSearchInfo.SearchCondition searchInfo = QuestionSearchCondition.toInfo(
            searchCondition);

        // search
        Page<Question> questions = questionReader.searchQuestions(projectId, searchInfo);

        // Page -> Response Dto
        return QuestionSearch.Response.toDto(questions);
    }

    /**
     * 업무 관리 게시글 서비스 - 상세 조회
     *
     * @param questionId 업무 관리 게시글 ID
     * @return QuestionListDto
     */
    @Override
    public QuestionGet.Response getById(Long questionId) {

        // find Question Entity
        Question question = questionReader.getQuestionById(questionId)
            .orElseThrow(QuestionNotFoundEntityException::new);

        // Entity -> Dto
        return QuestionGet.Response.toDto(question);
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
        Question initQuestion = questionReader.getQuestionById(taskBoardId)
            .orElseThrow(QuestionNotFoundEntityException::new);

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
        Question initQuestion = questionReader.getQuestionById(taskBoardId)
            .orElseThrow(QuestionNotFoundEntityException::new);

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
        Question initQuestion = questionReader.getQuestionById(taskBoardId)
            .orElseThrow(QuestionNotFoundEntityException::new);

        // update
        String title = request.getTitle();
        String content = request.getContent();
        initQuestion.update(title, content);

        // save
        Question updatedQuestion = questionStore.store(initQuestion);

        // Entity -> Dto
        return QuestionItemDto.toDto(updatedQuestion);
    }

    public List<QuestionCounter.Response> countByProgressStep(Long projectId) {

        // TODO: project id 로 project 조회

        // project 에 해당하는 progressStep 조회
        List<ProgressStep> steps = progressStepReader.getByProjectId(projectId);

        // progressStep 에 해당하는 question 의 개수 조회
        List<QuestionCounter.Response> list = new ArrayList<>();

        // 전체 question 의 개수 조회
        QuestionCounter.Response allCount = QuestionCounter.Response.makeAllCount(
            questionReader.countQuestionsByProject(projectId));
        list.add(allCount);

        for (ProgressStep step : steps) {
            QuestionCounter.Response dto = QuestionCounter.Response.toDto(step,
                questionReader.countQuestionsByProgressStep(projectId, step.getId()));
            list.add(dto);
        }
        return list;
    }
}
